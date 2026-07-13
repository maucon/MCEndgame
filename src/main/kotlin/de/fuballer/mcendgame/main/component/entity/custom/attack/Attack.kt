package de.fuballer.mcendgame.main.component.entity.custom.attack

import com.geckolib.animatable.GeoEntity
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.AttackAnimationData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.DelayedAttackData
import de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition.TriggerCondition
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.BlockAbleMovementMob
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

open class Attack<T>(
    val animationData: AttackAnimationData,
    val totalDuration: Int,
    val cooldown: Int,
    private val trigger: TriggerCondition,
    private val data: List<DelayedAttackData>,
    private val blockMovementDuration: Int = 0,
) where T : Mob, T : GeoEntity {
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

    open fun getAttackDataInstances(target: LivingEntity?) = data.mapNotNull { it.getInstance(target) }
}