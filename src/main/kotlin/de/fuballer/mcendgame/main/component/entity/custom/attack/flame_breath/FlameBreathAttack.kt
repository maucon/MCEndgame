package de.fuballer.mcendgame.main.component.entity.custom.attack.flame_breath

import de.fuballer.mcendgame.main.component.entity.custom.attack.Attack
import de.fuballer.mcendgame.main.component.entity.custom.attack.damage.DelayedAttackDamage
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.AttackAnimationData
import de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition.TriggerCondition
import de.fuballer.mcendgame.main.component.entity.custom.sound.DelayedSoundData
import de.maucon.mauconframework.event.EventGateway
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.mob.MobEntity
import software.bernie.geckolib.animatable.GeoEntity
import kotlin.math.atan2

class FlameBreathAttack<T>(
    animationData: AttackAnimationData,
    totalDuration: Int,
    cooldown: Int,
    trigger: TriggerCondition,
    damage: List<DelayedAttackDamage>,
    val damageConversion: Double = 1.0,
    val delay: Int,
    val duration: Int,
    val angle: Double,
    val entityWidthOffsetFactor: Double,
    val entityHeightOffsetFactor: Double,
    sounds: List<DelayedSoundData> = listOf(),
    blockMovementDuration: Int = 0,
) : Attack<T>(animationData, totalDuration, cooldown, trigger, damage, sounds, blockMovementDuration) where T : MobEntity, T : GeoEntity {
    constructor(
        animationData: AttackAnimationData,
        totalDuration: Int,
        cooldown: Int,
        trigger: TriggerCondition,
        damage: DelayedAttackDamage?,
        damageConversion: Double = 1.0,
        delay: Int,
        duration: Int,
        angle: Double,
        entityWidthOffsetFactor: Double,
        entityHeightOffsetFactor: Double,
        sounds: List<DelayedSoundData> = listOf(),
        blockMovementDuration: Int = 0,
    ) : this(
        animationData,
        totalDuration,
        cooldown,
        trigger,
        if (damage != null) listOf(damage) else listOf(),
        damageConversion,
        delay,
        duration,
        angle,
        entityWidthOffsetFactor,
        entityHeightOffsetFactor,
        sounds,
        blockMovementDuration,
    )

    override fun start(attacker: T, target: LivingEntity?) {
        super.start(attacker, target)

        if (target != null) {
            attacker.getLookControl().lookAt(target, 360F, 360F)

            val diff = target.eyePos.subtract(attacker.pos)

            val dx = diff.x
            val dy = diff.y
            val dz = diff.z

            val distanceXZ = kotlin.math.sqrt(dx * dx + dz * dz)

            attacker.yaw = (Math.toDegrees(atan2(dz, dx)) - 90.0).toFloat()
            attacker.pitch = (-Math.toDegrees(atan2(dy, distanceXZ))).toFloat()

            attacker.bodyYaw = attacker.yaw
            attacker.headYaw = attacker.yaw
        }

        val event = FlameBreathAttackEvent(attacker, target, damageConversion, delay, duration, angle, entityWidthOffsetFactor, entityHeightOffsetFactor)
        EventGateway.publish(event)
    }
}