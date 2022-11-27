package com.guflimc.brick.texticons.common.heads;

import com.guflimc.brick.scheduler.api.Scheduler;
import com.guflimc.brick.texticons.api.TextIconsAPI;
import com.guflimc.brick.texticons.common.BrickTextIconsManager;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public abstract class AbstractHeadsManager {

    private final Map<UUID, TextHeadRecord> cache = new ConcurrentHashMap<>();

    private record TextHeadRecord(Component head, Instant expireAt) {
    }

    public final static BufferedImage STEVE;
    static {
        try (
                InputStream is = BrickTextIconsManager.class.getClassLoader().getResourceAsStream("steve.png");
        ) {
            STEVE = ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //

    public AbstractHeadsManager(Scheduler scheduler) {
        scheduler.asyncRepeating(() -> {
            Instant now = Instant.now();
            cache.keySet().removeIf(id -> cache.get(id).expireAt != null && cache.get(id).expireAt.isBefore(now));
        }, 1, 1, TimeUnit.MINUTES);
    }

    //

    public CompletableFuture<Void> join(@NotNull UUID playerId, @NotNull String playerName) {
        return download(playerId).thenAccept(image -> {
            TextIconsAPI.get().addIcon("head_" + playerName, image);
            cache.put(playerId, new TextHeadRecord(TextIconsAPI.get().parse(image, 0, 0), null));
        });
    }

    public void quit(@NotNull UUID playerId, @NotNull String playerName) {
        TextIconsAPI.get().removeIcon("head_" + playerName);
        TextHeadRecord record = cache.get(playerId);
        cache.put(playerId, new TextHeadRecord(record.head, Instant.now().plus(5, ChronoUnit.MINUTES)));
    }

    //

    public CompletableFuture<Component> head(@NotNull UUID playerId) {
        if ( cache.containsKey(playerId) ) {
            return CompletableFuture.completedFuture(cache.get(playerId).head);
        }

        return download(playerId).thenApply(image -> {
            Component head = TextIconsAPI.get().parse(image, 0, 0);
            cache.put(playerId, new TextHeadRecord(head, Instant.now().plus(5, ChronoUnit.MINUTES)));
            return head;
        });
    }

    public CompletableFuture<BufferedImage> download(@NotNull UUID playerId) {
        return CompletableFuture.supplyAsync(() -> {
            URL textureUrl = skinUrl(playerId).join();
            if (textureUrl == null) {
                return STEVE;
            }

            /*
            TODO support for heads with a 3d layer

            int face1Rgb = image.getRGB(x, y);
            int face2Rgb = image.getRGB(x + 32, y);
            playerHead[y - 8][x - 8] = color(face2Rgb == 0 ? face1Rgb : face2Rgb);
             */

            try (InputStream is = textureUrl.openStream()) {
                return ImageIO.read(is).getSubimage(8, 8, 8, 8);
            } catch (IOException e) {
                return STEVE;
            }
        });
    }

    protected abstract CompletableFuture<URL> skinUrl(@NotNull UUID playerId);

}
