package de.fuballer.mcendgame.main.component.entity.custom.attack.fire_geysers

import de.fuballer.mcendgame.main.component.entity.custom.attack.Attack
import de.fuballer.mcendgame.main.component.entity.custom.attack.damage.DelayedAttackDamage
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.AttackAnimationData
import de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition.TriggerCondition
import de.fuballer.mcendgame.main.component.entity.custom.sound.DelayedSoundData
import de.maucon.mauconframework.event.EventGateway
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import software.bernie.geckolib.animatable.GeoEntity

class FireGeysersAttack<T>(
    animationData: AttackAnimationData,
    totalDuration: Int,
    cooldown: Int,
    trigger: TriggerCondition,
    damage: List<DelayedAttackDamage>,
    val burstDamageConversion: Double,
    val durationDamageConversion: Double,
    val delay: Int,
    val radius: Int,
    val geyserProbability: Double, // probability for each possible location to have a geyser
    val indicatorDuration: Int,
    val pillarDuration: Int,
    val geyserCountLimit: Int = Int.MAX_VALUE,
    sounds: List<DelayedSoundData> = listOf(),
    blockMovementDuration: Int = 0,
) : Attack<T>(animationData, totalDuration, cooldown, trigger, damage, sounds, blockMovementDuration) where T : Mob, T : GeoEntity {
    constructor(
        animationData: AttackAnimationData,
        totalDuration: Int,
        cooldown: Int,
        trigger: TriggerCondition,
        damage: DelayedAttackDamage?,
        burstDamageConversion: Double,
        durationDamageConversion: Double,
        delay: Int,
        radius: Int,
        geyserProbability: Double,
        indicatorDuration: Int,
        pillarDuration: Int,
        geyserCountLimit: Int = Int.MAX_VALUE,
        sounds: List<DelayedSoundData> = listOf(),
        blockMovementDuration: Int = 0,
    ) : this(
        animationData,
        totalDuration,
        cooldown,
        trigger,
        if (damage != null) listOf(damage) else listOf(),
        burstDamageConversion,
        durationDamageConversion,
        delay,
        radius,
        geyserProbability,
        indicatorDuration,
        pillarDuration,
        geyserCountLimit,
        sounds,
        blockMovementDuration,
    )

    override fun start(attacker: T, target: LivingEntity?) {
        super.start(attacker, target)

        val event = FireGeysersAttackEvent(attacker, target, burstDamageConversion, durationDamageConversion, delay, radius, geyserProbability, geyserCountLimit, indicatorDuration, pillarDuration)
        EventGateway.publish(event)
    }
}