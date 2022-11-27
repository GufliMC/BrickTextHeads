package com.guflimc.brick.texticons.spigot.pack;

import com.guflimc.brick.texticons.common.BrickTextIconsConfig;
import com.guflimc.brick.texticons.common.pack.AbstractResourcepackManager;
import org.bukkit.entity.Player;

public class SpigotResourcepackManager extends AbstractResourcepackManager<Player> {

    public SpigotResourcepackManager(BrickTextIconsConfig config) {
        super(config);
    }

    @Override
    public void force(Player player) {
        player.setResourcePack(resourcePack().url(), resourcePack().hashBytes(), true);
    }

}
