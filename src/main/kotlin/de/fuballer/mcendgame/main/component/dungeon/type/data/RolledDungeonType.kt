package de.fuballer.mcendgame.main.component.dungeon.type.data

import de.fuballer.mcendgame.main.component.dungeon.generation.layout.DungeonLayoutType
import de.fuballer.mcendgame.main.component.entity.EntityTypeStats
import de.fuballer.mcendgame.main.util.random.RandomOption
import net.minecraft.world.entity.LivingEntity

data class RolledDungeonType(
    val layoutType: DungeonLayoutType,
    val entityTypes: List<RandomOption<EntityTypeStats>>,
    val bossEntityTypes: List<EntityTypeStats>,
    val applyMisc: (List<LivingEntity>) -> Unit,
)