package com.guflimc.brick.texticons.minestom;

import com.google.gson.Gson;
import com.guflimc.brick.placeholders.minestom.api.MinestomPlaceholderAPI;
import com.guflimc.brick.scheduler.minestom.api.MinestomScheduler;
import com.guflimc.brick.texticons.minestom.heads.MinestomHeadsManager;
import com.guflimc.brick.texticons.minestom.pack.MinestomResourcepackManager;
import com.guflimc.brick.texticons.api.TextIconsAPI;
import com.guflimc.brick.texticons.common.BrickTextIconsConfig;
import com.guflimc.brick.texticons.common.BrickTextIconsManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extensions.Extension;

import java.io.IOException;
import java.io.InputStreamReader;

public class MinestomBrickTextIcons extends Extension {

    @Override
    public void initialize() {
        getLogger().info("Enabling " + nameAndVersion() + ".");

        BrickTextIconsConfig config;
        try (
                InputStreamReader r = new InputStreamReader(getResource("config.json"));
        ) {
            config = new Gson().fromJson(r, BrickTextIconsConfig.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // scheduler
        MinestomScheduler scheduler = new MinestomScheduler(getOrigin().getName());

        // heads manager
        MinestomHeadsManager headsManager = new MinestomHeadsManager(scheduler);

        // resource packs
        MinestomResourcepackManager resourcepackManager = new MinestomResourcepackManager(config);

        // text icons manager
        BrickTextIconsManager manager = new BrickTextIconsManager(headsManager);
        TextIconsAPI.setManager(manager);

        // join & quit event for caching
        new MinestomConnectionListener(resourcepackManager, headsManager);

        // register placeholders
        if (MinecraftServer.getExtensionManager().hasExtension("brickplaceholders")) {
            MinestomPlaceholderAPI.get().registerReplacer("player_text_head",
                    (player) -> manager.head(player.getUuid()).join());
        }

        getLogger().info("Enabled " + nameAndVersion() + ".");
    }

    @Override
    public void terminate() {
        getLogger().info("Disabled " + nameAndVersion() + ".");
    }

    private String nameAndVersion() {
        return getOrigin().getName() + " v" + getOrigin().getVersion();
    }

}
