package de.fuballer.mcendgame.main.messaging.dungeon

import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import de.fuballer.mcendgame.main.component.item.custom.crystal.CrystalItem

data class DungeonBossCrystalDropCommand(
    val dungeonLevel: Int,
    val aspects: Map<AspectItem, Int>,
    val crystalItems: MutableList<CrystalItem>,
    val lootMultiplier: Double,
)