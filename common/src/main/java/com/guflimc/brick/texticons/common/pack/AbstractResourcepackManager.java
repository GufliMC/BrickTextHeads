package com.guflimc.brick.texticons.common.pack;

import com.guflimc.brick.texticons.common.BrickTextIconsConfig;

import java.util.HexFormat;

public abstract class AbstractResourcepackManager<T> {

    public record ResourcePack(String url, String hash) {
        public byte[] hashBytes() {
            return HexFormat.of().parseHex(hash);
        }
    }

    private final ResourcePack resourcePack;
    private final boolean forced;

    public AbstractResourcepackManager(BrickTextIconsConfig config) {
        this.resourcePack = new ResourcePack(config.resourcePack.url, config.resourcePack.hash);
        this.forced = config.resourcePack.force;
    }

    public ResourcePack resourcePack() {
        return resourcePack;
    }

    public boolean shouldForce() {
        return forced;
    }

    public abstract void force(T player);

}
