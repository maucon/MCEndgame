package de.fuballer.mcendgame.component.dungeon.generation

import de.fuballer.mcendgame.component.dungeon.generation.layout_generator.LayoutGenerator
import de.fuballer.mcendgame.component.dungeon.generation.layout_generator.linear.LinearLayoutGenerator
import de.fuballer.mcendgame.component.dungeon.generation.layout_generator.linear.RoomTypes

enum class DungeonMapType(
    val layoutGeneratorProvider: () -> LayoutGenerator
) {
    STRONGHOLD(
        { LinearLayoutGenerator(RoomTypes.STRONGHOLD_START_ROOM, RoomTypes.STRONGHOLD_BOSS_ROOM, RoomTypes.STRONGHOLD_ROOMS) }
    )
}