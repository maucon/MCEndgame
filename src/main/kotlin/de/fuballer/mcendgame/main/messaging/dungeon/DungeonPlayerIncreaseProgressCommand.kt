package de.fuballer.mcendgame.main.messaging.dungeon

import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem

data class DungeonPlayerIncreaseProgressCommand(
    val aspects: Map<AspectItem, Int>,
    var progressGranted: Int = 1,
    var progressBlocked: Boolean = false,
)