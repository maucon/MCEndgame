package de.fuballer.mcendgame.main.component.entity.custom.attack.data

import de.fuballer.mcendgame.main.component.entity.custom.attack.damage.AttackDamage
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

open class DelayedDamageData(
    val damage: AttackDamage,
    val minDelay: Int,
    val maxDelay: Int = minDelay,
) : DelayedAttackData() {
    override fun getInstance(target: LivingEntity?): DelayedAttackDataInstance? {
        if (damage.requiresTarget() && target == null) return null
        return DelayedDamageDataInstance(this, damage)
    }

    override fun apply(
        level: ServerLevel,
        entity: Mob,
        target: LivingEntity?,
    ) {
        damage.apply(level, entity, target)
    }

    override fun shouldCancel(entity: Mob) = false
}