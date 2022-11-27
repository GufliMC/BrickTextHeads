package com.guflimc.brick.texticons.minestom.pack;

import com.guflimc.brick.texticons.common.BrickTextIconsConfig;
import com.guflimc.brick.texticons.common.pack.AbstractResourcepackManager;
import net.minestom.server.entity.Player;

public class MinestomResourcepackManager extends AbstractResourcepackManager<Player> {

    public MinestomResourcepackManager(BrickTextIconsConfig config) {
        super(config);
    }

    @Override
    public void force(Player player) {
        player.setResourcePack(net.minestom.server.resourcepack.ResourcePack
                .forced(resourcePack().url(), resourcePack().hash()));
    }
}
