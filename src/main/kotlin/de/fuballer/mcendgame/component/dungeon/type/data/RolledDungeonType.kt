package de.fuballer.mcendgame.component.dungeon.type.data

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import de.fuballer.mcendgame.component.dungeon.generation.DungeonMapType
import de.fuballer.mcendgame.util.random.RandomOption

data class RolledDungeonType(
    val mapType: DungeonMapType,
    val entityTypes: List<RandomOption<CustomEntityType>>,
    val bossEntityTypes: List<CustomEntityType>
)