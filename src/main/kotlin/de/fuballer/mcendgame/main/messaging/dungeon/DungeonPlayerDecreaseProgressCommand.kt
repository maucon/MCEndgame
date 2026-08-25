package de.fuballer.mcendgame.main.messaging.dungeon

import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem

data class DungeonPlayerDecreaseProgressCommand(
    val aspects: Map<AspectItem, Int>,
    var decrease: Int = 3,
    var decreaseBlocked: Boolean = false,
)