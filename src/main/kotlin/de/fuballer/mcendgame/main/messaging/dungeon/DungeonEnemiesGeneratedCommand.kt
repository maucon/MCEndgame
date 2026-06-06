package de.fuballer.mcendgame.main.messaging.dungeon

import de.fuballer.mcendgame.main.component.item.custom.aspect.AspectItem
import de.fuballer.mcendgame.main.util.extension.mixin.WorldMixinExtension.getDungeonAspects
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity

data class DungeonEnemiesGeneratedCommand(
    val world: ServerLevel,
    val enemies: List<LivingEntity>,
    val aspects: Map<AspectItem, Int>,
) {
    companion object {
        fun of(world: ServerLevel, enemies: List<LivingEntity>) = DungeonEnemiesGeneratedCommand(world, enemies, world.getDungeonAspects())
    }
}