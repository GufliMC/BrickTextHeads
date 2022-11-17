package com.guflimc.brick.textheads.spigot;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.guflimc.brick.textheads.common.BrickTextHeadsManager;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerProfile;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SpigotBrickTextHeadsManager extends BrickTextHeadsManager {

    public SpigotBrickTextHeadsManager(File configFile) throws IOException {
        super(configFile);
    }

    @Override
    protected CompletableFuture<URL> retrieveSkinUrl(@NotNull UUID playerId) {
        return CompletableFuture.supplyAsync(() -> {
            Player player = Bukkit.getPlayer(playerId);
            PlayerProfile profile;
            if (player != null) {
                profile = player.getPlayerProfile();
            } else {
                profile = Bukkit.createPlayerProfile(UUID.randomUUID(), RandomStringUtils.randomAlphanumeric(16));
            }
            URL skin = profile.getTextures().getSkin();
            if (skin == null) {
                skin = offlinePlayer(playerId).join();
            }
            return skin;
        });
    }

    private CompletableFuture<URL> offlinePlayer(@NotNull UUID playerId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/"
                        + playerId.toString().replace("-", ""));
                try (InputStreamReader isr = new InputStreamReader(url.openStream())) {
                    JsonElement json = JsonParser.parseReader(isr);
                    if ( json == null ) {
                        return null;
                    }
                    JsonObject texture = json.getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
                    JsonObject value = JsonParser.parseString(new String(Base64.getDecoder().decode(texture.get("value").getAsString()))).getAsJsonObject();
                    String result = value.getAsJsonObject("SKIN").get("url").getAsString();
                    return new URL(result);
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
    }

}
