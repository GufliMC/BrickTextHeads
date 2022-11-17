package com.guflimc.brick.textheads.minestom;

import com.guflimc.brick.textheads.common.BrickTextHeadsManager;
import net.minestom.server.entity.PlayerSkin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MinestomBrickTextHeadManager extends BrickTextHeadsManager {

    public MinestomBrickTextHeadManager(InputStream configInput) throws IOException {
        super(configInput);
    }

    @Override
    protected CompletableFuture<URL> retrieveSkinUrl(@NotNull UUID playerId) {
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
                throw new RuntimeException(e);
            }
        });
    }

}
