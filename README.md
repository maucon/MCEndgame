[![Minecraft Version](https://img.shields.io/badge/Minecraft-26.2-brightgreen)](https://www.minecraft.net/)
[![Mod Loader](https://img.shields.io/badge/Loader-Fabric-blue)](https://fabricmc.net/use/installer/)
[![Modrinth](https://img.shields.io/badge/Modrinth-MCEndgame-green)](https://modrinth.com/mod/mcendgame)
[![CurseForge](https://img.shields.io/badge/CurseForge-MCEndgame-orange)](https://www.curseforge.com/minecraft/mc-mods/mcendgame)
[![build](https://github.com/maucon/MCEndgame/actions/workflows/build.yml/badge.svg)](https://github.com/maucon/MCEndgame/actions/workflows/build.yml)
[![License](https://img.shields.io/badge/License-MIT-lightblue)](LICENSE)

<div align="center">
  <img src="assets/images/banner.png" alt="MCEndgame Banner" width="850">

  <p align="center">
    <a href="https://modrinth.com/mod/mcendgame">Get it on Modrinth</a>
    &middot;
    <a href="https://www.curseforge.com/minecraft/mc-mods/mcendgame">Get it on CurseForge</a>
    &middot;
    <a href="https://github.com/maucon/MCEndgame/issues/new?labels=bug&template=bug-report.md">Report Bug</a>
    &middot;
    <a href="https://github.com/maucon/MCEndgame/issues/new?labels=enhancement&template=feature-request.md">Request Feature</a>
  </p>
</div>

<br>

## About The Project

**MCEndgame** is a Minecraft mod that aims to make Minecraft's endgame more engaging through repeatable dungeon content, meaningful gear progression, and ARPG-inspired itemization.

[Watch the Trailer](https://www.youtube.com/watch?v=R2MLI5XDsfM)

### Features

- **Procedurally generated dungeons with difficulty scaling**

  Dungeons are entered through a Dungeon Device and generated using a room-based system, creating a different layout for each run. They feature custom enemies, including special variants
  with unique mechanics and loot drops, while difficulty scaling keeps the encounters challenging as players progress.

- **Boss fights with unique AI, attack patterns, and animations**

  Each boss is designed around unique mechanics, requiring players to learn attack patterns and adapt their strategy rather than simply relying on gear progression.

- **Custom armor sets, weapons, and attribute system**

  The mod includes a custom attribute system that allows items to have unique stats and effects beyond Minecraft's vanilla equipment system. This enables more complex itemization and allows
  gear to be built around different playstyles.

- **Totem slots and dungeon-specific power progression**

  Totems provide additional bonuses while inside dungeons, allowing players to increase their power for endgame content without affecting the balance of the vanilla Minecraft experience
  outside of dungeons.

- **Crystals and the Crystal Forge**

  Crystals are crafting items used to modify equipment, allowing players to change attribute rolls, upgrade existing attributes, or corrupt items for a chance at unique outcomes with
  different risks and rewards.

- **Aspects**

  Aspects are items socketed into the Dungeon Device to modify a dungeon before you enter. They can introduce additional bosses, increase enemy strength and loot drops, raise special enemy
  spawn rates, and more, letting players customize risk and reward for each run.

- **And more...**

### Useful Commands

- `/dungeonfilter` – Configure which item types will not be picked up when in a dungeon
- `/killer` – See the equipment and status effects of your latest killer
- `/totems` – Manage your currently equipped totems
- `/giveunique` (Moderator) – Generate a unique item with custom rolls
- `/dungeonlevel` (Moderator) – Set the current dungeon level and progress of a player
- `/givetotem` (Moderator) – Generate a specific totem

### Gallery

<details>
    <summary>Dungeon Device</summary>
    <img src="assets/images/dungeons.png" alt="Dungeon Device">
</details>
<details>
    <summary>Bosses</summary>
    <img src="assets/images/bosses.png" alt="Bosses">
</details>
<details>
    <summary>Scaling Difficulty</summary>
    <img src="assets/images/difficulty.png" alt="Scaling Difficulty">
</details>
<details>
    <summary>Custom Items</summary>
    <img src="assets/images/items.png" alt="Custom Items">
</details>
<details>
    <summary>Custom Attribute System</summary>
    <img src="assets/images/attributes.png" alt="Custom Attribute System">
</details>
<details>
    <summary>Crystals</summary>
    <img src="assets/images/crystals.png" alt="Crystals">
</details>
<details>
    <summary>Aspects</summary>
    <img src="assets/images/aspects.png" alt="Aspects">
</details>
<details>
    <summary>Killer</summary>
    <img src="assets/images/killer.png" alt="Killer">
</details>

### Analytics

MCEndgame collects anonymous gameplay data to help improve the mod. No personal information is collected.
See the [Analytics Wiki Page](https://github.com/maucon/MCEndgame/wiki/Analytics) for details on what is collected and how to opt out.

---

## Getting Started

### Installation

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft **26.2**
2. Download [Fabric API](https://modrinth.com/mod/fabric-api)
3. Download MCEndgame from [Releases](https://github.com/maucon/MCEndgame/releases), [Modrinth](https://modrinth.com/mod/mcendgame/)
   or [CurseForge](https://www.curseforge.com/minecraft/mc-mods/mcendgame)
4. Download all [required dependencies](#dependencies)
5. Place all `.jar` files into your mods folder
6. Launch the game

### Dependencies

| Dependency                                                                | Version                |
|---------------------------------------------------------------------------|------------------------|
| [Fabric Loader](https://fabricmc.net/use/)                                | ≥ 0.19.3               |
| [Fabric API](https://modrinth.com/mod/fabric-api)                         | ≥ 0.152.1+26.2         |
| [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin) | ≥ 1.13.12+kotlin.2.4.0 |
| [Geckolib](https://modrinth.com/mod/geckolib)                             | ~5.5.3                 |

### Building from Source

You can also build the mod yourself:

```bash
git clone https://github.com/maucon/MCEndgame.git
cd MCEndgame
./gradlew build
```

The built mod `.jar` will be in `build/libs/`.
> Note: Building the project requires a valid GitHub token with `read:packages` permission, provided via the `GITHUB_PACKAGES_TOKEN` environment variable, along with your GitHub username set
> in `GITHUB_PACKAGES_NAME`.

---

## Contributing

If you have a feature request or found a bug, please open an issue. If you'd like to contribute a fix or improvement, feel free to fork the repository and submit a pull request.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## Contact

Should you have any questions or encounter any difficulties, please don't hesitate to open an issue or join the `Discussions` section.

## Acknowledgments

* [FabricMC](https://fabricmc.net/)
* [Geckolib](https://modrinth.com/mod/geckolib/versions)
* [NucleoidMC/runtimeWorlds](https://github.com/NucleoidMC/runtimeWorlds)
* [Path of Exile](https://www.pathofexile.com/)
* [Best README Template](https://github.com/othneildrew/Best-README-Template)
