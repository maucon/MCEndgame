package de.fuballer.mcendgame.main.messaging.dungeon

import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem

data class DungeonBossSpawnPortalCommand(
    val aspects: Map<AspectItem, Int>,
    var shouldCreatePortal: Boolean = true,
)