package de.fuballer.mcendgame.main.component.entity.custom.attack.flame_breath

import com.geckolib.animatable.GeoEntity
import de.fuballer.mcendgame.main.component.entity.custom.attack.Attack
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.AttackAnimationData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.DelayedAttackData
import de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition.TriggerCondition
import de.maucon.mauconframework.event.EventGateway
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import kotlin.math.atan2

class FlameBreathAttack<T>(
    animationData: AttackAnimationData,
    totalDuration: Int,
    cooldown: Int,
    trigger: TriggerCondition,
    data: List<DelayedAttackData>,
    val damageConversion: Double = 1.0,
    val delay: Int,
    val duration: Int,
    val angle: Double,
    val entityWidthOffsetFactor: Double,
    val entityHeightOffsetFactor: Double,
    blockMovementDuration: Int = 0,
) : Attack<T>(animationData, totalDuration, cooldown, trigger, data, blockMovementDuration) where T : Mob, T : GeoEntity {
    override fun start(attacker: T, target: LivingEntity?) {
        super.start(attacker, target)

        if (target != null) {
            attacker.getLookControl().setLookAt(target, 360F, 360F)

            val diff = target.eyePosition.subtract(attacker.position())

            val dx = diff.x
            val dy = diff.y
            val dz = diff.z

            val distanceXZ = kotlin.math.sqrt(dx * dx + dz * dz)

            attacker.yRot = (Math.toDegrees(atan2(dz, dx)) - 90.0).toFloat()
            attacker.xRot = (-Math.toDegrees(atan2(dy, distanceXZ))).toFloat()

            attacker.yBodyRot = attacker.yRot
            attacker.yHeadRot = attacker.yRot
        }

        val event = FlameBreathAttackEvent(attacker, target, damageConversion, delay, duration, angle, entityWidthOffsetFactor, entityHeightOffsetFactor)
        EventGateway.publish(event)
    }
}