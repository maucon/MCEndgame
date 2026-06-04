package de.fuballer.mcendgame.main.component.entity.custom.attack.teleport

import de.fuballer.mcendgame.main.component.entity.custom.attack.Attack
import de.fuballer.mcendgame.main.component.entity.custom.attack.damage.DelayedAttackDamage
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.AttackAnimationData
import de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition.TriggerCondition
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.TeleportAttackMob
import de.fuballer.mcendgame.main.component.entity.custom.sound.DelayedSoundData
import de.maucon.mauconframework.event.EventGateway
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import software.bernie.geckolib.animatable.GeoEntity

class TeleportToTargetAttack<T>(
    animationData: AttackAnimationData,
    totalDuration: Int,
    cooldown: Int,
    trigger: TriggerCondition,
    damage: List<DelayedAttackDamage>,
    val teleportDelayTicks: Int,
    val choseLocationDelayTicks: Int,
    sounds: List<DelayedSoundData> = listOf(),
    blockMovementDuration: Int = 0,
) : Attack<T>(animationData, totalDuration, cooldown, trigger, damage, sounds, blockMovementDuration) where T : Mob, T : GeoEntity {
    constructor(
        animationData: AttackAnimationData,
        totalDuration: Int,
        cooldown: Int,
        trigger: TriggerCondition,
        damage: DelayedAttackDamage?,
        teleportDelay: Int,
        choseLocationDelayTicks: Int,
        sounds: List<DelayedSoundData> = listOf(),
        blockMovementDuration: Int = 0,
    ) : this(
        animationData,
        totalDuration,
        cooldown,
        trigger,
        if (damage != null) listOf(damage) else listOf(),
        teleportDelay,
        choseLocationDelayTicks,
        sounds,
        blockMovementDuration,
    )

    override fun start(attacker: T, target: LivingEntity?) {
        super.start(attacker, target)

        if (target == null) return
        if (attacker !is TeleportAttackMob) return
        val event = TeleportAttackEvent(attacker, target, choseLocationDelayTicks, teleportDelayTicks)
        EventGateway.publish(event)
    }
}