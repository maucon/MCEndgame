package de.fuballer.mcendgame.main.messaging.dungeon

import de.fuballer.mcendgame.main.component.dungeon.type.DungeonType
import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem

data class SelectDungeonTypeCommand(
    var dungeonType: DungeonType,
    val aspects: Map<AspectItem, Int>,
)