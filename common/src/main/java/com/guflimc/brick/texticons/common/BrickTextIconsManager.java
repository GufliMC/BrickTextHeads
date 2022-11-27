package com.guflimc.brick.texticons.common;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.guflimc.brick.texticons.api.TextIconsManager;
import com.guflimc.brick.texticons.api.domain.TextIcon;
import com.guflimc.brick.texticons.common.heads.AbstractHeadsManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.TextColor.color;

public class BrickTextIconsManager implements TextIconsManager {

    private final Map<String, TextIcon> icons = new ConcurrentHashMap<>();
    private final AbstractHeadsManager headsManager;

    public BrickTextIconsManager(AbstractHeadsManager headsManager) {
        this.headsManager = headsManager;

        // Load default icons
        String[] names;
        try (
                InputStream is = getClass().getClassLoader().getResourceAsStream("100icons.json");
                InputStreamReader isr = new InputStreamReader(is)
        ) {
            JsonArray array = JsonParser.parseReader(isr).getAsJsonArray();
            names = new String[array.size()];
            for ( int i = 0; i < array.size(); i++ ) {
                names[i] = array.get(i).getAsString();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (
                InputStream is = getClass().getClassLoader().getResourceAsStream("100icons.png");
        ) {
            BufferedImage image = ImageIO.read(is);
            addIcons(names, image, 10, 10, 4, 4);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public CompletableFuture<Component> head(@NotNull UUID playerId) {
        return headsManager.head(playerId);
    }

    @Override
    public Collection<TextIcon> icons() {
        return Collections.unmodifiableCollection(icons.values());
    }

    @Override
    public Optional<TextIcon> findIcon(@NotNull String name) {
        return Optional.ofNullable(icons.get(name.toLowerCase()));
    }

    @Override
    public void addIcon(@NotNull String name, @NotNull BufferedImage image) {
        name = name.toLowerCase();
        icons.put(name, new TextIcon(name, parse(image, 0, 0)));
    }

    @Override
    public void addIcons(@NotNull String[] names, @NotNull BufferedImage sheet, int hAmount, int vAmount, int hGap, int vGap) {
        if ( sheet.getWidth() != hAmount * 8 + (hAmount - 1) * hGap
                || sheet.getHeight() != vAmount * 8 + (vAmount - 1) * vGap ) {
            throw new IllegalArgumentException("This sheet is not large enough for the given amount of icons.");
        }

        for (int i = 0; i < names.length; i++) {
            String name = names[i].toLowerCase();
            icons.put(name, new TextIcon(name, parse(sheet,
                    (i % hAmount) * (hGap + 8),
                    (i / hAmount) * (vGap + 8))));
        }
    }

    @Override
    public void removeIcon(@NotNull String name) {
        icons.remove(name.toLowerCase());
    }

    @Override
    public Component parse(@NotNull BufferedImage image, int offsetX, int offsetY) {
        if (image.getWidth() < offsetX + 8 || image.getHeight() < offsetY + 8) {
            throw new IllegalArgumentException("The image is too small to contain an 8x8 icon at the given coordinate.");
        }

        int[][] skin = new int[8][8];
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                int rgb = image.getRGB(x + offsetX, y + offsetY);
                skin[y][x] = rgb;
            }
        }

        Component component = Component.empty();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                int rgb = skin[y][x];
                if ( rgb == 0 ) {
                    // TODO add transparent pixels to resource pack
                    component = component.append(text((char) (((int) '\uF810') + y)));
                } else {
                    component = component.append(text((char) (((int) '\uF810') + y))
                            .color(color(rgb)));  // pixel of color
                }
                component = component
                        .append(text('\uE001')); // space between pixels
            }
            component = component.append(text('\uE008')); // new line
        }

        return component.append(text("\uF008")); // reset
    }
}
