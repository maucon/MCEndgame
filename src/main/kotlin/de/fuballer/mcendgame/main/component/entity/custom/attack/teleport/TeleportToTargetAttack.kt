package de.fuballer.mcendgame.main.component.entity.custom.attack.teleport

import com.geckolib.animatable.GeoEntity
import de.fuballer.mcendgame.main.component.entity.custom.attack.Attack
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.AttackAnimationData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.DelayedAttackData
import de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition.TriggerCondition
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.TeleportAttackMob
import de.maucon.mauconframework.event.EventGateway
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

class TeleportToTargetAttack<T>(
    animationData: AttackAnimationData,
    totalDuration: Int,
    cooldown: Int,
    trigger: TriggerCondition,
    data: List<DelayedAttackData>,
    val teleportDelayTicks: Int,
    val choseLocationDelayTicks: Int,
    blockMovementDuration: Int = 0,
) : Attack<T>(animationData, totalDuration, cooldown, trigger, data, blockMovementDuration) where T : Mob, T : GeoEntity {

    override fun start(attacker: T, target: LivingEntity?) {
        super.start(attacker, target)

        if (target == null) return
        if (attacker !is TeleportAttackMob) return
        val event = TeleportAttackEvent(attacker, target, choseLocationDelayTicks, teleportDelayTicks)
        EventGateway.publish(event)
    }
}