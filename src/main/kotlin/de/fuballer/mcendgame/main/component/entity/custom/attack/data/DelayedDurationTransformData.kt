package de.fuballer.mcendgame.main.component.entity.custom.attack.data

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

class DelayedDurationTransformData(
    val durationStart: Int,
    val durationEnd: Int = durationStart,
    private val transform: (ServerLevel, Mob, LivingEntity?, Int, Double) -> Unit,
) : DelayedAttackData(durationStart) {
    override fun getInstance(
        target: LivingEntity?,
        attackSpeed: Double,
    ): DelayedDurationDataInstance = DelayedDurationDataInstance(this, attackSpeed)

    fun apply(
        level: ServerLevel,
        entity: Mob,
        target: LivingEntity?,
        age: Int,
        attackSpeed: Double,
    ) {
        transform(level, entity, target, age, attackSpeed)
    }
}