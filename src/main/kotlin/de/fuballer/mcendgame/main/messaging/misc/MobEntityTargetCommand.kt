package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.level.Level

data class MobEntityTargetCommand(
    val world: Level,
    val entity: Mob,
    val target: LivingEntity?,
    var canTarget: Boolean = true,
) {
    companion object {
        fun of(entity: Mob, target: LivingEntity?) = MobEntityTargetCommand(entity.level(), entity, target)
    }
}