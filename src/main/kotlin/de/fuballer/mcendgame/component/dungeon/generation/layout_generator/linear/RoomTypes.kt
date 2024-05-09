package de.fuballer.mcendgame.component.dungeon.generation.layout_generator.linear

object RoomTypes {
    val STRONGHOLD_START_ROOM = RoomTypeLoader.load("stronghold/stronghold_start_1000_0", 0)
    val STRONGHOLD_BOSS_ROOM = RoomTypeLoader.load("stronghold/stronghold_boss_0010_0", 0)

    val STRONGHOLD_ROOMS = listOf(
        RoomTypeLoader.load("stronghold/stronghold_large_0011_0", 10),
        RoomTypeLoader.load("stronghold/stronghold_large_1010_0", 10),
        RoomTypeLoader.load("stronghold/stronghold_large_1010_1", 10),
        RoomTypeLoader.load("stronghold/stronghold_large_1010_2", 10),
        RoomTypeLoader.load("stronghold/stronghold_medium_0110_0", 10),
        RoomTypeLoader.load("stronghold/stronghold_medium_0110_1", 10),
        RoomTypeLoader.load("stronghold/stronghold_medium_1110_0", 10),
        RoomTypeLoader.load("stronghold/stronghold_small_0110_0", 10),
        RoomTypeLoader.load("stronghold/stronghold_small_1010_0", 10),
        RoomTypeLoader.load("stronghold/stronghold_small_1010_1", 10),
    )
}