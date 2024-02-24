# MCEndgame

MCEndgame is a Minecraft server plugin that enhances the endgame experience by introducing a new level of progression.

![Active Map Device](/img/map_device.png)

## Features

### Dungeons

Dungeons can be accessed through a map device, offering a variety of dungeon types and maps, each filled with monsters.
To complete a dungeon, players must defeat the boss at the end.
Monsters and bosses drop special loot, adding a dungeon-crawling experience to Minecraft.

![Dungeon Boss "Demonic Golem"](/img/demonic_golem.png)

### Custom Monster

MCEndgame introduces new monsters equipped with custom attacks, including bosses that cast special abilities,
adding a layer of challenge and uniqueness to encounters.

![Custom Monster "Reaper"](/img/reaper.png)

### Custom Items

Monsters inside a dungeon drop items with an expanded array of attributes, alongside entirely custom items boasting unique attributes.
These enhancements enrich the player experience both inside and outside of dungeons.

![Custom Item "Geistergaloschen"](/img/geistergaloschen.jpg)

### Artifacts

Artifacts, which drop rarely from monsters within dungeons, can boost the players stats inside a dungeons.
This, combined with custom items, offers a wealth of build choices for players.

![Artifact of Impact](/img/artifact.jpg)

### Corruption

Orbs of Corruption, dropped by dungeon bosses, offer a high-risk, high-reward mechanic for item modification.
These orbs can unpredictably enhance items, potentially degrading them or even causing their destruction,
allowing players to min-max their gear with a gamble.

![Corrupted Bow](/img/corrupted_bow.jpg)

## Commands

* `/killer`: Provides information about the last mob that eliminated you
* `/dungeon-remaining`: Displays details on the number of mobs still alive within the dungeon
* `/dungeon-filter`: Opens the filter page, allowing you to manage item blacklisting
* `/dungeon-artifacts`: Opens the artifact page, showcasing and changing equipped artifacts
* `/dungeon-artifact-give`: Generates a new artifact for the player
* `/item-info`: Presents details about the stat rolls of the item currently held in the main hand
* `/dungeon-statistics`: Exhibits dungeon statistics, including the count of monsters slain
* `/dungeon-progress`: Indicates or modifies the current progress of a player within the dungeon
* `/dungeon-type`: Indicates or modifies the next generated dungeon-type of a player
* `/give-custom-item`: Gives a custom item with the specified type to a player
* `/dungeon-seed`: View or set the seed of the next dungeon a player will generate

For more info about the commands see `plugin.yml`

## Getting Started

You can download a pre-built Jar via the `Releases` section or build it yourself.

### Building the Plugin Jar

1. Clone the repo
2. Setup Gradle
3. Run the Gradle task `fatJar`

## Usage

1. Set up your Minecraft [Paper](https://papermc.io/) Server
2. Download [FastAsyncWorldEdit](https://www.spigotmc.org/resources/fastasyncworldedit.13932/) (Bukkit Version)
3. Download [ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/)
4. Place the downloaded libraries into the plugins folder
5. Place the `MCEndgame` Jar into the plugins folder
6. Start the server
7. Activate the [Texture Pack](/mce-texture-pack) (Optional)

## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

Distributed under the GNU GPLv3 License. See `LICENSE.txt` for more information.

## Contact

Should you have any questions or encounter any difficulties, please don't hesitate to open an issue or join the `Discussions` section.

## Acknowledgments

* [SpigotMC](https://hub.spigotmc.org/)
* [FastAsyncWorldEdit](https://github.com/IntellectualSites/FastAsyncWorldEdit)
* [ProtocolLib](https://github.com/aadnk/ProtocolLib)


* [Choose an Open Source License](https://choosealicense.com/)
* [README-Template](https://github.com/othneildrew/Best-README-Template)
