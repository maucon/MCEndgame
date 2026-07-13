package de.fuballer.mcendgame.main.component.entity.custom.attack.fire_geysers

import com.geckolib.animatable.GeoEntity
import de.fuballer.mcendgame.main.component.entity.custom.attack.Attack
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.AttackAnimationData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.DelayedAttackData
import de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition.TriggerCondition
import de.maucon.mauconframework.event.EventGateway
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

class FireGeysersAttack<T>(
    animationData: AttackAnimationData,
    totalDuration: Int,
    cooldown: Int,
    trigger: TriggerCondition,
    data: List<DelayedAttackData>,
    val burstDamageConversion: Double,
    val durationDamageConversion: Double,
    val delay: Int,
    val radius: Int,
    val geyserProbability: Double, // probability for each possible location to have a geyser
    val indicatorDuration: Int,
    val pillarDuration: Int,
    val geyserCountLimit: Int = Int.MAX_VALUE,
    blockMovementDuration: Int = 0,
) : Attack<T>(animationData, totalDuration, cooldown, trigger, data, blockMovementDuration) where T : Mob, T : GeoEntity {

    override fun start(attacker: T, target: LivingEntity?) {
        super.start(attacker, target)

        val event =
            FireGeysersAttackEvent(attacker, target, burstDamageConversion, durationDamageConversion, delay, radius, geyserProbability, geyserCountLimit, indicatorDuration, pillarDuration)
        EventGateway.publish(event)
    }
}