package de.fuballer.mcendgame.component.dungeon.generation.layout_generator.linear

import de.fuballer.mcendgame.util.random.RandomOption

object RoomTypes {
    val STRONGHOLD_START_ROOM = RoomTypeLoader.load("stronghold/start")
    val STRONGHOLD_BOSS_ROOM = RoomTypeLoader.load("stronghold/boss")

    val STRONGHOLD_ROOMS = listOf(
        RandomOption(4, RoomTypeLoader.load("stronghold/arena")),
        RandomOption(4, RoomTypeLoader.load("stronghold/bridge_dive_plantrooms")),
        RandomOption(1, RoomTypeLoader.load("stronghold/decayed-staircase_branching")),
        RandomOption(7, RoomTypeLoader.load("stronghold/flat_chandelier_branching")),
        RandomOption(3, RoomTypeLoader.load("stronghold/inverted-pyramid_curve")),
        RandomOption(1, RoomTypeLoader.load("stronghold/parkour_curve")),
        RandomOption(3, RoomTypeLoader.load("stronghold/sewer")),
        RandomOption(4, RoomTypeLoader.load("stronghold/slimebounce")),
        RandomOption(7, RoomTypeLoader.load("stronghold/small_connector_curve")),
        RandomOption(7, RoomTypeLoader.load("stronghold/small_connector_sloped")),
        RandomOption(5, RoomTypeLoader.load("stronghold/stair_chandelier_sidedrop_curve")),
        RandomOption(7, RoomTypeLoader.load("stronghold/stair_elevated_branching")),
        RandomOption(3, RoomTypeLoader.load("stronghold/stairs_statue")),
        RandomOption(4, RoomTypeLoader.load("stronghold/tight_hanging-lamps_curve")),
        RandomOption(7, RoomTypeLoader.load("stronghold/tiny_flat_connector")),
        RandomOption(4, RoomTypeLoader.load("stronghold/tunnelbridge_curve")),
        RandomOption(4, RoomTypeLoader.load("stronghold/zigzag-stairs_ponds")),
    )
}