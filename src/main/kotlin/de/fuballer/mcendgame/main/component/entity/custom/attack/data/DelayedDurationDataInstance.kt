package de.fuballer.mcendgame.main.component.entity.custom.attack.data

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

class DelayedDurationDataInstance(
    private val durationData: DelayedDurationTransformData,
) : DelayedAttackDataInstance(durationData) {
    override fun tick(
        level: ServerLevel,
        entity: Mob,
        target: LivingEntity?,
    ): Boolean {
        if (durationData.shouldCancel(entity)) return true

        age++
        if (age < durationData.durationStart) return false
        if (age > durationData.durationEnd) return true

        durationData.apply(level, entity, target, age)
        return false
    }
}