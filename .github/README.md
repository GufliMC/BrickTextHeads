# BrickTextHeads

A simple Minecraft plugin/extension for text heads in the chat, sidebar, bossbar...

## Credits

Original demo and proof of concept by [CatDevz](https://github.com/CatDevz)

## Install

Get the [release](https://github.com/GufliMC/BrickTextHeads/releases) and place it in your server.

A custom resource pack is required (1.87 kB), this can be automatically installed with a config option. You can
also download it [here](https://download.mc-packs.net/pack/6facffa13fba481def8d3d0c35dea6ef21eebf8b.zip).

### API

#### Gradle

```
repositories {
    maven { url "https://repo.jorisg.com/snapshots" }
}

dependencies {
    compileOnly 'com.guflimc.brick.textheads:api:+'
}
```

#### Javadocs

Check the javadocs for all platforms [here](https://guflimc.github.io/BrickChat/).

#### Examples

```java
// You only need the player's UUID. This will return a kyori adventure's Component.
TextHeadsAPI.get().head(player.getUniqueId()).thenAccept(head-> {
        player.sendMessage(head);
});
```

