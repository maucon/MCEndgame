package de.fuballer.mcendgame.main.component.entity.custom.attack.data

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

abstract class DelayedAttackData(
    val delay: Int = 0,
) {
    abstract fun getInstance(target: LivingEntity?): DelayedAttackDataInstance?

    abstract fun apply(world: ServerLevel, entity: Mob, target: LivingEntity?)

    open fun shouldCancel(entity: Mob): Boolean = !entity.isAlive
}