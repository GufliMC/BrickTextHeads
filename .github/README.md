# BrickTextIcons

A simple Minecraft plugin/extension for text icons in the chat, sidebar, bossbar, actionbar...

![example image](https://user-images.githubusercontent.com/8937042/202451733-c6aaddaf-2f40-4174-891b-324cca738146.png)

## Credits

Original demo and proof of concept by [CatDevz](https://github.com/CatDevz)

## Install

Get the [release](https://github.com/GufliMC/BrickTextIcons/releases) and place it in your server.

A custom resource pack is required (1.87 kB), this can be automatically installed with a config option. You can
also download it [here](https://download.mc-packs.net/pack/6facffa13fba481def8d3d0c35dea6ef21eebf8b.zip).

This plugin integrates well with the existing [Brick](https://github.com/GufliMC) ecosystem.
* [BrickPlaceholders](https://github.com/GufliMC/BrickPlaceholders)
* [BrickChat](https://github.com/GufliMC/BrickChat)
* [BrickSidebar](https://github.com/GufliMC/BrickSidebar)

## API

#### Gradle

```
repositories {
    maven { url "https://repo.jorisg.com/snapshots" }
}

dependencies {
    compileOnly 'com.guflimc.brick.texticons:api:+'
}
```

#### Javadocs

Check the javadocs for all platforms [here](https://guflimc.github.io/BrickTextIcons/).

#### Examples

```java
// You only need the player's UUID. This will return a kyori adventure's Component.
TextIconsAPI.get().head(player.getUniqueId()).thenAccept(head-> {
        player.sendMessage(head);
});
```

