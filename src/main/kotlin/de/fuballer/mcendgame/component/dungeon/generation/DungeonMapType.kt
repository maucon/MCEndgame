package de.fuballer.mcendgame.component.dungeon.generation

import de.fuballer.mcendgame.component.dungeon.generation.layout_generator.LayoutGenerator
import de.fuballer.mcendgame.component.dungeon.generation.layout_generator.linear.LinearLayoutGenerator
import de.fuballer.mcendgame.component.dungeon.generation.layout_generator.linear.RoomTypeLoader

private val startRoom = RoomTypeLoader.load("stronghold/stronghold_start_1000_0", 0)
private val rooms = listOf(
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

enum class DungeonMapType(
    val layoutGeneratorProvider: () -> LayoutGenerator
) {
    STRONGHOLD({ LinearLayoutGenerator(startRoom, rooms) })
}