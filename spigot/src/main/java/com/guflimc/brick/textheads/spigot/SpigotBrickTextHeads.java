package com.guflimc.brick.textheads.spigot;

import com.guflimc.brick.placeholders.spigot.api.SpigotPlaceholderAPI;
import com.guflimc.brick.textheads.api.TextHeadsAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class SpigotBrickTextHeads extends JavaPlugin {

    @Override
    public void onEnable() {
        saveResource("config.json", false);

        // register chat manager
        SpigotBrickTextHeadsManager manager;
        try {
            manager = new SpigotBrickTextHeadsManager(new File(getDataFolder(), "config.json"));
            TextHeadsAPI.setManager(manager);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // register events
        getServer().getPluginManager().registerEvents(new SpigotConnectionListener(manager), this);

        // register placeholders
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("BrickPlaceholders")) {
            SpigotPlaceholderAPI.get().registerReplacer("player_text_head",
                    (player) -> manager.head(player.getUniqueId()).join());
        }

        getLogger().info("Enabled " + nameAndVersion() + ".");
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabled " + nameAndVersion() + ".");
    }

    private String nameAndVersion() {
        return getName() + " v" + getDescription().getVersion();
    }

}
