package de.fuballer.mcendgame.main.component.entity.custom.attack.damage.instance

import de.fuballer.mcendgame.main.component.entity.custom.attack.damage.AttackDamage
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

class LeapAttackDamageInstance(
    minDelay: Int,
    maxDelay: Int,
    target: LivingEntity?,
    damage: AttackDamage,
) : AttackDamageInstance(minDelay, maxDelay, target, damage) {
    override fun shouldCancel(damager: Mob) = damager.onGround()
}