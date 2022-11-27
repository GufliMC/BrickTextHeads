package com.guflimc.brick.texticons.spigot.listeners;

import com.guflimc.brick.texticons.spigot.heads.SpigotHeadsManager;
import com.guflimc.brick.texticons.spigot.pack.SpigotResourcepackManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    private final SpigotResourcepackManager resourcepackManager;
    private final SpigotHeadsManager headsManager;

    public ConnectionListener(SpigotResourcepackManager resourcepackManager, SpigotHeadsManager headsManager) {
        this.resourcepackManager = resourcepackManager;
        this.headsManager = headsManager;

        Bukkit.getOnlinePlayers().forEach(p ->
                headsManager.join(p.getUniqueId(), p.getName()));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        headsManager.join(event.getUniqueId(), event.getName()).join();
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        if (resourcepackManager.shouldForce()) {
            resourcepackManager.force(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        headsManager.quit(event.getPlayer().getUniqueId(), event.getPlayer().getName());
    }
}
