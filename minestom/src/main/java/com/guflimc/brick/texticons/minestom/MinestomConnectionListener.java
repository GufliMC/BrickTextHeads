package com.guflimc.brick.texticons.minestom;

import com.guflimc.brick.texticons.minestom.heads.MinestomHeadsManager;
import com.guflimc.brick.texticons.minestom.pack.MinestomResourcepackManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerLoginEvent;
import org.jetbrains.annotations.NotNull;

public class MinestomConnectionListener {

    private final MinestomResourcepackManager resourcepackManager;
    private final MinestomHeadsManager headsManager;

    public MinestomConnectionListener(MinestomResourcepackManager resourcepackManager, MinestomHeadsManager headsManager) {
        this.resourcepackManager = resourcepackManager;
        this.headsManager = headsManager;

        MinecraftServer.getGlobalEventHandler().addListener(PlayerLoginEvent.class, this::onPlayerLogin);
        MinecraftServer.getGlobalEventHandler().addListener(PlayerDisconnectEvent.class, this::onPlayerDisconnect);

        MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(p ->
                headsManager.join(p.getUuid(), p.getUsername()));
    }

    private void onPlayerLogin(@NotNull PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (resourcepackManager.shouldForce()) {
            resourcepackManager.force(player);
        }

        headsManager.join(player.getUuid(), player.getUsername());
    }

    private void onPlayerDisconnect(@NotNull PlayerDisconnectEvent event) {
        headsManager.quit(event.getPlayer().getUuid(), event.getPlayer().getUsername());
    }
}
