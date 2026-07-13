package de.fuballer.mcendgame.main.component.entity.custom.attack.data

import de.fuballer.mcendgame.main.component.entity.custom.attack.damage.AttackDamage
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

open class DelayedDamageDataInstance(
    private val damageData: DelayedDamageData,
    private val damage: AttackDamage,
) : DelayedAttackDataInstance(damageData) {
    override fun tick(
        world: ServerLevel,
        entity: Mob,
        target: LivingEntity?,
    ): Boolean {
        if (damageData.shouldCancel(entity)) return true

        age++
        if (age < damageData.minDelay) return false
        if (age > damageData.maxDelay) return true

        return damage.apply(world, entity, target)
    }
}