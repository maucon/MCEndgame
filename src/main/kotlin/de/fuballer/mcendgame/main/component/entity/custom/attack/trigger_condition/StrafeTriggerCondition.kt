package de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition

import de.fuballer.mcendgame.main.util.extension.mixin.MoveControlMixinExtension.getStrafeForwards
import de.fuballer.mcendgame.main.util.extension.mixin.MoveControlMixinExtension.getStrafeRight
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

class StrafeTriggerCondition(
    val minRight: Float = Float.NEGATIVE_INFINITY,
    val maxRight: Float = Float.POSITIVE_INFINITY,
    val minForwards: Float = Float.NEGATIVE_INFINITY,
    val maxForwards: Float = Float.POSITIVE_INFINITY,
) : TriggerCondition() {
    override fun doesTrigger(
        attacker: Mob,
        target: LivingEntity?,
    ): Boolean {
        val moveControl = attacker.moveControl
        val strafeRight = moveControl.getStrafeRight()
        if (strafeRight < minRight) return false
        if (strafeRight > maxRight) return false
        val strafeForwards = moveControl.getStrafeForwards()
        if (strafeForwards < minForwards) return false
        if (strafeForwards > maxForwards) return false
        return true
    }
}