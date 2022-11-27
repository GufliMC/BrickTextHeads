package com.guflimc.brick.texticons.minestom.heads;

import com.guflimc.brick.scheduler.api.Scheduler;
import com.guflimc.brick.texticons.common.heads.AbstractHeadsManager;
import net.minestom.server.entity.PlayerSkin;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MinestomHeadsManager extends AbstractHeadsManager {

    public MinestomHeadsManager(Scheduler scheduler) {
        super(scheduler);
    }

    @Override
    protected CompletableFuture<URL> skinUrl(@NotNull UUID playerId) {
        return CompletableFuture.supplyAsync(() -> {
            PlayerSkin playerSkin = PlayerSkin.fromUuid(playerId.toString());

            if (playerSkin == null) {
                return null;
            }

            String json = new String(Base64.getDecoder().decode(playerSkin.textures()));
            String url = json.split("\"SKIN\"")[1].split("\"")[3];
            try {
                return new URL(url);
            } catch (MalformedURLException e) {
                return null;
            }
        });
    }
}
