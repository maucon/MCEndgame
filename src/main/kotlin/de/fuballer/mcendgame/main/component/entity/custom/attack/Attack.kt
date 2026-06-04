package de.fuballer.mcendgame.main.component.entity.custom.attack

import com.geckolib.animatable.GeoEntity
import de.fuballer.mcendgame.main.component.entity.custom.attack.damage.DelayedAttackDamage
import de.fuballer.mcendgame.main.component.entity.custom.attack.damage.instance.AttackDamageInstance
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.AttackAnimationData
import de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition.TriggerCondition
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.BlockAbleMovementMob
import de.fuballer.mcendgame.main.component.entity.custom.sound.DelayedSoundData
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

open class Attack<T>(
    val animationData: AttackAnimationData,
    val totalDuration: Int,
    val cooldown: Int,
    private val trigger: TriggerCondition,
    private val damage: List<DelayedAttackDamage>,
    private val sounds: List<DelayedSoundData> = listOf(),
    private val blockMovementDuration: Int = 0,
) where T : Mob, T : GeoEntity {
    constructor(
        animationData: AttackAnimationData,
        totalDuration: Int,
        cooldown: Int,
        trigger: TriggerCondition,
        damage: DelayedAttackDamage?,
        sounds: List<DelayedSoundData> = listOf(),
        blockMovementDuration: Int = 0,
    ) : this(
        animationData,
        totalDuration,
        cooldown,
        trigger,
        if (damage != null) listOf(damage) else listOf(),
        sounds,
        blockMovementDuration,
    )

    open fun canStart(
        attacker: Mob,
        target: LivingEntity?,
    ) = trigger.doesTrigger(attacker, target)

    open fun start(
        attacker: T,
        target: LivingEntity?,
    ) {
        animationData.triggerAnimation(attacker)

        if (blockMovementDuration == 0) return
        val blockAbleMovementMob = attacker as? BlockAbleMovementMob<*> ?: return
        blockAbleMovementMob.blockMovement(blockMovementDuration)
    }

    open fun getDamageInstances(
        target: LivingEntity?,
    ): List<AttackDamageInstance> {
        val instances = mutableListOf<AttackDamageInstance>()
        damage.forEach {
            if (it.damage.requiresTarget() && target == null) return@forEach

            val damageInstance = getDamageInstance(target, it)
            instances.add(damageInstance)
        }
        return instances
    }

    open fun getDamageInstance(
        target: LivingEntity?,
        delayedDamage: DelayedAttackDamage,
    ) = AttackDamageInstance(delayedDamage.minDelay, delayedDamage.maxDelay, target, delayedDamage.damage)

    open fun getSoundInstances() = sounds.map(DelayedSoundData::getInstance)
}