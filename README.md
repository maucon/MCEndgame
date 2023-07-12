# MC-Endgame

## About the Project

MC-Endgame is a Minecraft server plugin that enhances the endgame experience by introducing a new level of progression.

Once players have successfully defeated the wither and obtained a sufficient amount of netherite ingots, they gain the ability to craft a special item called the `Map-Device`. This device opens
portals, granting access to a series of increasingly challenging dungeons. These dungeons not only provide formidable foes but also offer intriguing and valuable treasures to be
discovered along the way.

## Features

MC-Endgame seamlessly integrates with the existing Minecraft system without altering or disrupting the core gameplay experience.
The plugin functions as an extension, solely dedicated to enriching the endgame content.

By implementing MC-Endgame, players can engage in an immersive dungeon grind, strategically navigating challenging environments to acquire superior equipment.
Rest assured, the plugin does not interfere with or compromise the regular gameplay, preserving the familiar Minecraft mechanics while offering a thrilling endgame progression.

#### Dungeons

![dungeon with enemies](https://media.discordapp.net/attachments/807226379049238539/1128666363033038868/2023-07-12_13.27.29.png?width=961&height=270)

Dungeons within MC-Endgame are dynamically generated structures, ensuring a unique experience each time.
By utilizing the `Map-Device`, players can unlock new dungeons, triggering the appearance of portals that transport them directly into these
challenging environments. It's important to note that dungeon worlds are entirely separate from the regular Minecraft worlds.

As you navigate through the treacherous dungeons, you'll face relentless hordes of hostile creatures. Your ultimate goal is to confront and
defeat the formidable boss lurking within each dungeon. Upon successfully vanquishing the boss, portals will emerge, providing a means of
returning to the location of your Map-Device. It's worth noting that defeating the boss also contributes to your progress towards unlocking
higher tiers of dungeons.

![the dungeon boss](https://media.discordapp.net/attachments/807226379049238539/1128666362525524029/2023-07-12_13.28.32.png?width=961&height=270)

Advancing through the ranks in MC-Endgame is based on successfully completing three dungeons. Each triumphant trio of dungeons results in a tier upgrade.
Higher-tier dungeons pose greater challenges, but they also offer more abundant and valuable loot. However, it's important to exercise caution,
as death within a dungeon will result in a rank downgrade and the loss of progress towards the next tier.

MC-Endgame ensures an exhilarating journey through its dynamic dungeons, rewarding your efforts with increased difficulty, better rewards, and the
constant drive to climb the ranks.

#### Loot

Within the dungeons of MC-Endgame, monsters have the potential to drop gear that possesses unique characteristics.
This gear stands out due to its ability to contain additional stats, enhancing the performance of the player. For example, armor pieces might
provide extra health, while boots could grant a movement speed boost. Notably, it's even possible to acquire normally mutually exclusive enchantments
on a single armor piece, such as different protection enchants.

Furthermore, dungeon bosses offer a chance to obtain a valuable item known as the `Heart of Corruption`. This item holds the power to corrupt
equipment items in intriguing ways. Through the use of an anvil, a corruption process can be initiated, leading to changes in the base item.
This process allows for the rerolling of stat rolls, the potential gain or loss of enchantment levels, and even the possibility of item destruction.
It's important to note that once an item has been corrupted, it gains a `Corrupted` tag, preventing further modifications. As you progress to higher
tiers, more powerful versions of the `Heart of Corruption` will be dropped by bosses. These enhanced versions have the capacity to corrupt an item twice,
providing the potential for a +2 level increase in an enchantment. However, it's worth mentioning that the risk of item destruction is also amplified
with the more powerful version of the `Heart of Corruption`.

<p>
<img src="https://media.discordapp.net/attachments/807226379049238539/1128666361871212635/Screenshot_2023-07-12_133411.png" width="212" height="300"  alt="dropped item"/>
<img src="https://media.discordapp.net/attachments/807226379049238539/1128666361548259398/Screenshot_2023-07-12_133249.png" width="200" height="300"  alt="corrupted item"/>
</p>

#### Killstreak

Engaging in combat within a dungeon of MC-Endgame initiates a killstreak system. Each monster slain contributes to the killstreak,
thereby increasing the likelihood of the monster dropping its gear. However, it is crucial to act swiftly, as the killstreak possesses a time limit,
and if you fail to eliminate monsters rapidly enough, the killstreak will expire and reset. Therefore, strategic efficiency is key to maximizing
your chances of obtaining gear from defeated monsters.
Stay vigilant, keep up the momentum, and seize the opportunity before the killstreak elapses.

#### Artifacts

Artifacts are rare items that have a chance to drop from monsters found within dungeons.
These special items can be stored in a dedicated inventory accessed through the `/dungeon-artifacts` command. Once obtained, artifacts bestow unique
buffs upon the player while they are inside a dungeon.
These buffs provide advantageous effects that can aid in combat, traversal, or various other aspects of the dungeon exploration experience.

#### Filter

As you spend time exploring a dungeon, your inventory may become cluttered with undesirable and unnecessary items.
To prevent the pickup of such unwanted items within the dungeon, you have the option to utilize the `/dungeon-filter` command.
This command allows you to manage a blacklist, specifying which items should not be picked up during your dungeon adventure.
By customizing this filter, you can ensure that your inventory remains free from the burden of junk items, allowing you to focus on collecting
and prioritizing the valuable treasures and resources within the dungeon.

### Commands

* `/killer`: Provides information about the last mob that eliminated you
* `/dungeon-remaining`: Displays details on the number of mobs still alive within the dungeon
* `/dungeon-filter`: Opens the filter page, allowing you to manage item blacklisting
* `/dungeon-artifacts`: Opens the artifact page, showcasing equipped artifacts
* `/dungeon-artifact-give`: Generates a new artifact for the player
* `/item-info`: Presents details about the stat rolls of the item currently held in the main hand
* `/dungeon-statistics`: Exhibits dungeon statistics, including the count of monsters slain
* `/dungeon-progress`: Indicates or modifies the current progress of a player within the dungeon
* `/dungeon-build-calculation`:  [Work In Progress] Provides damage calculations related to your current equipment setup

For more info about the commands see `plugin.yml`

## Getting Started

### Building the Plugin Jar

1. Clone the repo
2. Setup Gradle
3. Run the Gradle task `fatJar`

## Usage

1. Set up your Minecraft [Paper](https://papermc.io/) Server
2. Download [FastAsyncWorldEdit](https://www.spigotmc.org/resources/fastasyncworldedit.13932/) (Bukkit Version)
3. Place the `MC-Endgame` and `FastAsyncWorldEdit` Jar into the plugins folder
4. Create folder `MC-Endgame` in the `plugins` folder
5. Copy the `schematics` folder into the `plugins` folder (found `src/main/resources`)
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

* [FastAsyncWorldEdit]()
* [README-Template](https://github.com/othneildrew/Best-README-Template)
* [Choose an Open Source License](https://choosealicense.com/)
