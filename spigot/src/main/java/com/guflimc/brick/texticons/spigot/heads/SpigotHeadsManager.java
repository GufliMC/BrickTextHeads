package com.guflimc.brick.texticons.spigot.heads;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.guflimc.brick.scheduler.api.Scheduler;
import com.guflimc.brick.texticons.common.heads.AbstractHeadsManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SpigotHeadsManager extends AbstractHeadsManager {

    public SpigotHeadsManager(Scheduler scheduler) {
        super(scheduler);
    }

    @Override
    protected CompletableFuture<URL> skinUrl(@NotNull UUID playerId) {
        return CompletableFuture.supplyAsync(() -> {
            OfflinePlayer player = Bukkit.getOfflinePlayer(playerId);
            URL skin = player.getPlayerProfile().getTextures().getSkin();
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
                    if (json.isJsonNull()) {
                        return null;
                    }
                    JsonObject texture = json.getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
                    JsonObject value = JsonParser.parseString(new String(Base64.getDecoder().decode(texture.get("value").getAsString()))).getAsJsonObject();
                    String result = value.getAsJsonObject("SKIN").get("url").getAsString();
                    return new URL(result);
                }
            } catch (IOException ex) {
                return null;
            }
        }).exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
    }
}
