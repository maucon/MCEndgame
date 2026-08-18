package de.fuballer.mcendgame.main.component.entity.custom.attack.data

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

class DelayedDurationDataInstance(
    private val durationData: DelayedDurationTransformData,
    attackSpeed: Double,
) : DelayedAttackDataInstance(durationData, attackSpeed) {
    private val durationStart = (durationData.durationStart / attackSpeed).toInt()
    private val durationEnd = (durationData.durationEnd / attackSpeed).toInt()

    override fun tick(
        level: ServerLevel,
        entity: Mob,
        target: LivingEntity?,
    ): Boolean {
        if (durationData.shouldCancel(entity)) return true

        age++
        if (age < durationStart) return false
        if (age > durationEnd) return true

        durationData.apply(level, entity, target, age, attackSpeed)
        return false
    }
}