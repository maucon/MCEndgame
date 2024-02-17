# MCEndgame

## About the Project

MC-Endgame is a Minecraft server plugin that enhances the endgame experience by introducing a new level of progression.

![dungeon with enemies](https://media.discordapp.net/attachments/807226379049238539/1128666363033038868/2023-07-12_13.27.29.png?width=961&height=270)

![the dungeon boss](https://media.discordapp.net/attachments/807226379049238539/1128666362525524029/2023-07-12_13.28.32.png?width=961&height=270)

<p>
<img src="https://media.discordapp.net/attachments/807226379049238539/1128666361871212635/Screenshot_2023-07-12_133411.png" width="212" height="300"  alt="dropped item"/>
<img src="https://media.discordapp.net/attachments/807226379049238539/1128666361548259398/Screenshot_2023-07-12_133249.png" width="200" height="300"  alt="corrupted item"/>
</p>

### Commands

* `/killer`: Provides information about the last mob that eliminated you
* `/dungeon-remaining`: Displays details on the number of mobs still alive within the dungeon
* `/dungeon-filter`: Opens the filter page, allowing you to manage item blacklisting
* `/dungeon-artifacts`: Opens the artifact page, showcasing equipped artifacts
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
5. Place the `MC-Endgame` Jar into the plugins folder
6. Start the server

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

* [FastAsyncWorldEdit](https://github.com/IntellectualSites/FastAsyncWorldEdit)
* [ProtocolLib](https://github.com/aadnk/ProtocolLib)
* [README-Template](https://github.com/othneildrew/Best-README-Template)
* [Choose an Open Source License](https://choosealicense.com/)
