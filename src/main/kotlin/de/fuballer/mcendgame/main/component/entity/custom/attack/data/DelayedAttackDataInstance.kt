package de.fuballer.mcendgame.main.component.entity.custom.attack.data

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

open class DelayedAttackDataInstance(
    val data: DelayedAttackData,
    val attackSpeed: Double,
) {
    private var delay = (data.delay / attackSpeed).toInt()
    protected var age = 0

    open fun tick(
        level: ServerLevel,
        entity: Mob,
        target: LivingEntity?,
    ): Boolean {
        if (data.shouldCancel(entity)) return true

        age++
        if (age < delay) return false

        data.apply(level, entity, target)
        return true
    }
}