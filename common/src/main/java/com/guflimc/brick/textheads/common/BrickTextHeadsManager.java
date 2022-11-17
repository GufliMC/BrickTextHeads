package com.guflimc.brick.textheads.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.guflimc.brick.textheads.api.TextHeadsManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HexFormat;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.TextColor.color;

public abstract class BrickTextHeadsManager implements TextHeadsManager {

    private final Map<UUID, TextHeadRecord> cache = new ConcurrentHashMap<>();

    private record TextHeadRecord(Component head, Instant expireAt) { }

    public final static Component STEVE;
    static {
        try {
            STEVE = retrieve(BrickTextHeadsManager.class.getClassLoader().getResourceAsStream("steve.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Component retrieve(InputStream is) throws IOException {
        BufferedImage image;
        try ( is; ) {
            image = ImageIO.read(is);
        }

        TextColor[][] skin = new TextColor[8][8];
        for (int x = 8; x < 16; x++) {
            for (int y = 8; y < 16; y++) {
                int rgb = image.getRGB(x, y);
                skin[y - 8][x - 8] = color(rgb);
            }
        }

        Component component = Component.empty();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                component = component
                        .append(text((char) (((int) '\uF810') + y)).color(skin[y][x]))
                        .append(text('\uE001'));
            }
            component = component.append(text('\uE008'));
        }

        return component.append(text("\uF008"));
    }

    protected final static Gson gson = new GsonBuilder().create();

    private final BrickTextHeadsConfig config;

    public BrickTextHeadsManager(File configFile) throws IOException {
        this(new FileInputStream(configFile));
    }

    public BrickTextHeadsManager(InputStream configInput) throws IOException {
        try (
                configInput;
                InputStreamReader isr = new InputStreamReader(configInput);
        ) {
            this.config = gson.fromJson(isr, BrickTextHeadsConfig.class);
        }
    }

    //

    public record ResourcePack(String url, String hash) {

        public byte[] hashBytes() {
            return HexFormat.of().parseHex(hash);
        }

    }

    private final static ResourcePack DEFAULT_RESOURCE_PACK = new ResourcePack(
            "https://download.mc-packs.net/pack/6facffa13fba481def8d3d0c35dea6ef21eebf8b.zip",
            "6facffa13fba481def8d3d0c35dea6ef21eebf8b"
    );


    public ResourcePack resourcePack() {
        return DEFAULT_RESOURCE_PACK;
    }

    public BrickTextHeadsConfig config() {
        return config;
    }

    //

    @Override
    public final CompletableFuture<Component> head(@NotNull UUID playerId) {
        if (cache.containsKey(playerId)) {
            return CompletableFuture.completedFuture(cache.get(playerId).head());
        }
        return CompletableFuture.supplyAsync(() -> {
            URL textureUrl = retrieveSkinUrl(playerId).join();
            if ( textureUrl == null ) {
                return STEVE;
            }

            Component head;
            try {
                head = retrieve(textureUrl.openStream());
            } catch (IOException e) {
                head = STEVE;
            }

            cache.put(playerId, new TextHeadRecord(head, Instant.now().plus(5, ChronoUnit.MINUTES)));
            return head;
        });
    }

    protected abstract CompletableFuture<URL> retrieveSkinUrl(@NotNull UUID playerId);

    //

    public CompletableFuture<Void> join(@NotNull UUID playerId) {
        return head(playerId).thenAccept(head -> {
            cache.put(playerId, new TextHeadRecord(head, null));
        });
    }

    public void quit(@NotNull UUID playerId) {
        cache.remove(playerId);
    }
}
