package de.fuballer.mcendgame.main.component.entity.custom.attack.data

import de.fuballer.mcendgame.main.component.entity.custom.attack.damage.AttackDamage
import net.minecraft.world.entity.Mob

class DelayedLeapDamageData(
    damage: AttackDamage,
    minDelay: Int,
    maxDelay: Int = minDelay,
) : DelayedDamageData(damage, minDelay, maxDelay) {
    override fun shouldCancel(entity: Mob) = entity.onGround()
}