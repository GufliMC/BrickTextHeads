package com.guflimc.brick.textheads.minestom;

import com.guflimc.brick.placeholders.minestom.api.MinestomPlaceholderAPI;
import com.guflimc.brick.textheads.api.TextHeadsAPI;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extensions.Extension;

import java.io.IOException;

public class MinestomBrickTextHeads extends Extension {

    @Override
    public void initialize() {
        getLogger().info("Enabling " + nameAndVersion() + ".");

        MinestomBrickTextHeadManager manager;
        try {
            manager = new MinestomBrickTextHeadManager(getResource("config.json"));
            TextHeadsAPI.setManager(manager);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // join & quit event for caching
        new MinestomConnectionListener(manager);

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
