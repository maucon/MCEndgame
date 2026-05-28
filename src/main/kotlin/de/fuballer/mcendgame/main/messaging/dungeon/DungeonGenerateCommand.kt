package de.fuballer.mcendgame.main.messaging.dungeon

import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem

data class DungeonGenerateCommand(
    var dungeonLevel: Int,
    var enemyCount: Int,
    var bossCount: Int,
    val aspects: Map<AspectItem, Int>,
)