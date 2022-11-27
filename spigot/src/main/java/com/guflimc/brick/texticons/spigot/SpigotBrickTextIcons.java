package com.guflimc.brick.texticons.spigot;

import com.google.gson.Gson;
import com.guflimc.brick.placeholders.spigot.api.SpigotPlaceholderAPI;
import com.guflimc.brick.scheduler.spigot.api.SpigotScheduler;
import com.guflimc.brick.texticons.api.TextIconsAPI;
import com.guflimc.brick.texticons.common.BrickTextIconsConfig;
import com.guflimc.brick.texticons.common.BrickTextIconsManager;
import com.guflimc.brick.texticons.spigot.heads.SpigotHeadsManager;
import com.guflimc.brick.texticons.spigot.listeners.ChatListener;
import com.guflimc.brick.texticons.spigot.listeners.ConnectionListener;
import com.guflimc.brick.texticons.spigot.pack.SpigotResourcepackManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class SpigotBrickTextIcons extends JavaPlugin {

    @Override
    public void onEnable() {
        saveResource("config.json", false);

        BrickTextIconsConfig config;
        try (
                FileInputStream fis = new FileInputStream(new File(getDataFolder(), "config.json"));
                InputStreamReader r = new InputStreamReader(fis);
        ) {
            config = new Gson().fromJson(r, BrickTextIconsConfig.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // scheduler
        SpigotScheduler scheduler = new SpigotScheduler(this);

        // heads manager
        SpigotHeadsManager headsManager = new SpigotHeadsManager(scheduler);

        // resource packs
        SpigotResourcepackManager resourcepackManager = new SpigotResourcepackManager(config);

        // text icons manager
        BrickTextIconsManager manager = new BrickTextIconsManager(headsManager);
        TextIconsAPI.setManager(manager);

        // register events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new ConnectionListener(resourcepackManager, headsManager), this);
        pm.registerEvents(new ChatListener(), this);

        // register placeholders
        if (pm.isPluginEnabled("BrickPlaceholders")) {
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
