package com.guflimc.brick.texticons.api.domain;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public record TextIcon(@NotNull String name, @NotNull Component icon) {
}
