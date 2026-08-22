package de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.level.ClipContext
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3

class StraightPathToTargetTriggerCondition : TriggerCondition() {
    override fun doesTrigger(attacker: Mob, target: LivingEntity?): Boolean {
        if (target == null) return false
        if (!attacker.hasLineOfSight(target)) return false

        val attackerEyePos = Vec3(attacker.x, attacker.eyeY, attacker.z)
        val targetEyePos = Vec3(target.x, target.eyeY, target.z)

        val direction = targetEyePos.subtract(attackerEyePos).horizontal().normalize()
        val sideDirection = Vec3(-direction.z, 0.0, direction.x)

        val offsetFactor = attacker.bbWidth / 2 * 1.5

        return sequenceOf(-1.0, 1.0).all { positive ->
            val offset = sideDirection.scale(offsetFactor * positive)
            val offsetAttackerPos = attackerEyePos.add(offset)

            val context = ClipContext(offsetAttackerPos, targetEyePos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, attacker)
            attacker.level().clip(context).type == HitResult.Type.MISS
        }
    }
}