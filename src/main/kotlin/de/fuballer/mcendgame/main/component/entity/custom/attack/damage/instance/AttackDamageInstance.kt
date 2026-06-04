package de.fuballer.mcendgame.main.component.entity.custom.attack.damage.instance

import de.fuballer.mcendgame.main.component.entity.custom.attack.damage.AttackDamage
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

open class AttackDamageInstance(
    private var minDelay: Int,
    private val maxDelay: Int,
    private val target: LivingEntity?,
    private val damage: AttackDamage,
) {
    var age = 0

    // returns if the damage is applied, expired or cancelled
    fun tick(
        world: ServerLevel,
        damager: Mob,
    ): Boolean {
        if (shouldCancel(damager)) return true

        age++
        if (age < minDelay) return false
        if (age > maxDelay) return true

        return damage.apply(world, damager, target)
    }

    open fun shouldCancel(damager: Mob) = false
}