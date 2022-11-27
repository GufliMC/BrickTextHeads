package com.guflimc.brick.texticons.spigot.listeners;

import com.guflimc.brick.texticons.api.TextIconsAPI;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener implements Listener {

    private final Pattern pattern = Pattern.compile("(:[^:]+:)");

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Matcher m = pattern.matcher(message);

        StringBuilder sb = new StringBuilder();
        while ( m.find() ) {
            String group = m.group().toLowerCase();
            String iconName = group.substring(1, group.length() - 1); // remove surrounding colons
            String icon = icon(iconName);
            m.appendReplacement(sb, icon);
        }
        m.appendTail(sb);

        message = sb.toString();
        event.setMessage(message);
    }

    private String icon(String icon) {
        return TextIconsAPI.get().findIcon(icon)
                .map(i -> LegacyComponentSerializer.legacySection().serialize(i.icon()))
                .orElse(null);
    }

}
