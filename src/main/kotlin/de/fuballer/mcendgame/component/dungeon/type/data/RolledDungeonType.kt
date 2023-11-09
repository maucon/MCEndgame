package de.fuballer.mcendgame.component.dungeon.type.data

import de.fuballer.mcendgame.component.custom_entity.data.CustomEntityType
import de.fuballer.mcendgame.component.dungeon.boss.data.BossType
import de.fuballer.mcendgame.util.random.RandomOption

data class RolledDungeonType(
    val mapType: DungeonMapType,
    val entityTypes: List<RandomOption<CustomEntityType>>,
    val bossType: BossType
)
