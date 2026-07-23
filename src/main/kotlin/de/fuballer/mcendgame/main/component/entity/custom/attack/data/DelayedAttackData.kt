package de.fuballer.mcendgame.main.component.entity.custom.attack.data

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

abstract class DelayedAttackData(
    val delay: Int = 0,
) {
    open fun getInstance(target: LivingEntity?): DelayedAttackDataInstance? = DelayedAttackDataInstance(this)

    open fun apply(world: ServerLevel, entity: Mob, target: LivingEntity?) {}

    open fun shouldCancel(entity: Mob): Boolean = !entity.isAlive
}