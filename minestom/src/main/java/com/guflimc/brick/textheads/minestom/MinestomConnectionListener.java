package com.guflimc.brick.textheads.minestom;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.resourcepack.ResourcePack;
import org.jetbrains.annotations.NotNull;

public class MinestomConnectionListener {

    private final MinestomBrickTextHeadManager manager;

    public MinestomConnectionListener(MinestomBrickTextHeadManager manager) {
        this.manager = manager;

        MinecraftServer.getGlobalEventHandler().addListener(PlayerLoginEvent.class, this::onPlayerLogin);
        MinecraftServer.getGlobalEventHandler().addListener(PlayerDisconnectEvent.class, this::onPlayerDisconnect);

        MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(p -> manager.join(p.getUuid()));
    }

    private void onPlayerLogin(@NotNull PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (manager.config().forceResourcePack) {
            player.setResourcePack(ResourcePack.forced(manager.resourcePack().url(), manager.resourcePack().hash()));
        }

        manager.join(player.getUuid());
    }

    private void onPlayerDisconnect(@NotNull PlayerDisconnectEvent event) {
        manager.quit(event.getPlayer().getUuid());
    }
}
