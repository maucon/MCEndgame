package de.fuballer.mcendgame.main.component.entity.custom.attack.data

import de.fuballer.mcendgame.main.component.entity.custom.attack.damage.AttackDamage
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

open class DelayedDamageDataInstance(
    private val damageData: DelayedDamageData,
    private val damage: AttackDamage,
    attackSpeed: Double,
) : DelayedAttackDataInstance(damageData, attackSpeed) {
    private var minDelay = (damageData.minDelay / attackSpeed).toInt()
    private var maxDelay = (damageData.maxDelay / attackSpeed).toInt()

    override fun tick(
        level: ServerLevel,
        entity: Mob,
        target: LivingEntity?,
    ): Boolean {
        if (damageData.shouldCancel(entity)) return true

        age++
        if (age < minDelay) return false
        if (age > maxDelay) return true

        return damage.apply(level, entity, target)
    }
}