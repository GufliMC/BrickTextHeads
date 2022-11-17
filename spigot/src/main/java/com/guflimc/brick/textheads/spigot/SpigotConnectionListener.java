package com.guflimc.brick.textheads.spigot;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SpigotConnectionListener implements Listener {

    private final SpigotBrickTextHeadsManager manager;

    public SpigotConnectionListener(SpigotBrickTextHeadsManager manager) {
        this.manager = manager;

        Bukkit.getOnlinePlayers().forEach(p -> manager.join(p.getUniqueId()));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        manager.join(event.getUniqueId()).join();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        if ( manager.config().forceResourcePack ) {
            Player player = event.getPlayer();
            player.setResourcePack(manager.resourcePack().url(), manager.resourcePack().hashBytes(), true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        manager.quit(event.getPlayer().getUniqueId());
    }
}
