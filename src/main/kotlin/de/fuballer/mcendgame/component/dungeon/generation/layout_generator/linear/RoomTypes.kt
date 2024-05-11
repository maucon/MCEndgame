package de.fuballer.mcendgame.component.dungeon.generation.layout_generator.linear

import de.fuballer.mcendgame.util.random.RandomOption

object RoomTypes {
    val STRONGHOLD_START_ROOM = RoomTypeLoader.load("stronghold/stronghold_start_1000_0")
    val STRONGHOLD_BOSS_ROOM = RoomTypeLoader.load("stronghold/stronghold_boss_0010_0")

    val STRONGHOLD_ROOMS = listOf(
        RandomOption(4, RoomTypeLoader.load("stronghold/stronghold_large_0011_0")),
        RandomOption(4, RoomTypeLoader.load("stronghold/stronghold_large_1010_0")),
        RandomOption(4, RoomTypeLoader.load("stronghold/stronghold_large_1010_1")),
        RandomOption(4, RoomTypeLoader.load("stronghold/stronghold_large_1010_2")),
        RandomOption(7, RoomTypeLoader.load("stronghold/stronghold_medium_0110_0")),
        RandomOption(7, RoomTypeLoader.load("stronghold/stronghold_medium_0110_1")),
        RandomOption(7, RoomTypeLoader.load("stronghold/stronghold_medium_1110_0")),
        RandomOption(10, RoomTypeLoader.load("stronghold/stronghold_small_0110_0")),
        RandomOption(10, RoomTypeLoader.load("stronghold/stronghold_small_1010_0")),
        RandomOption(10, RoomTypeLoader.load("stronghold/stronghold_small_1010_1")),
    )
}