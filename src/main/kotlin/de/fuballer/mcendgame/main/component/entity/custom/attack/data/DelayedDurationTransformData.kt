package de.fuballer.mcendgame.main.component.entity.custom.attack.data

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

class DelayedDurationTransformData(
    val durationStart: Int,
    val durationEnd: Int = durationStart,
    private val transform: (ServerLevel, Mob, LivingEntity?, Int) -> Unit,
) : DelayedAttackData(durationStart) {
    override fun getInstance(target: LivingEntity?): DelayedDurationDataInstance = DelayedDurationDataInstance(this)

    fun apply(
        level: ServerLevel,
        entity: Mob,
        target: LivingEntity?,
        age: Int,
    ) {
        transform(level, entity, target, age)
    }
}