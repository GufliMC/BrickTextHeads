package com.guflimc.brick.textheads.api;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface TextHeadsManager {

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

}
