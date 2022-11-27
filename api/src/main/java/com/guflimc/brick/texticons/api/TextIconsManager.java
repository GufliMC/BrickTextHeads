package com.guflimc.brick.texticons.api;

import com.guflimc.brick.texticons.api.domain.TextIcon;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface TextIconsManager {

    /**
     * Returns the chat component representing the given player's head.
     * For online players, the head is instantly available and {@link CompletableFuture#get()}
     * can be used without blocking the main thread.
     * For offline players, each head is cached for 5 minutes so subsequent requests for the
     * same head are instantly available.
     *
     * @param playerId the player's UUID
     * @return a future that will complete with the head component
     */
    CompletableFuture<Component> head(@NotNull UUID playerId);

    /**
     * Returns a list of all loaded text icons.
     * @return A set of loaded text icons.
     */
    Collection<TextIcon> icons();

    /**
     * Returns the text icon for the given name.
     * @param name icon to search for
     * @return The text icon that matches this name
     */
    Optional<TextIcon> findIcon(@NotNull String name);

    /**
     * Register a text icon for the given name and image.
     * @param name The icon name
     * @param image The icon image
     */
    void addIcon(@NotNull String name, @NotNull BufferedImage image);

    /**
     * Register a set of text icons with the given names and sprite sheet.
     * @param names The icon names going from left to right, top to bottom
     * @param sheet The sprite sheet
     */
    void addIcons(@NotNull String[] names, @NotNull BufferedImage sheet, int hAmount, int vAmount, int hGap, int vGap);

    /**
     * Remove a text icon with the given name.
     * @param name The icon name
     */
    void removeIcon(@NotNull String name);

    /**
     * Parse the pixels of an image at the given x and y coordinate into a Text Component
     * @param image The image to parse
     * @param offsetX The starting x coordinate
     * @param offsetY The starting y coordinate
     * @return The parsed text component
     */
    Component parse(@NotNull BufferedImage image, int offsetX, int offsetY);

}
