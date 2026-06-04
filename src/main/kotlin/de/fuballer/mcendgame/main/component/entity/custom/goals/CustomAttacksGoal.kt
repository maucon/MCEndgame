package de.fuballer.mcendgame.main.component.entity.custom.goals

import de.fuballer.mcendgame.main.component.entity.custom.interfaces.CustomAttacksMob
import net.minecraft.world.entity.Mob
import software.bernie.geckolib.animatable.GeoEntity

class CustomAttacksGoal<T>(
    private val attacker: T,
) : DisableAbleGoal() where T : Mob, T : GeoEntity, T : CustomAttacksMob<T> {
    override fun canUse(): Boolean {
        if (!super.canUse()) return false
        val target = attacker.target ?: return false
        return target.isAlive
    }

    override fun canContinueToUse() = canUse()

    override fun tick() {
        if (!attacker.canAttack()) return
        val attack = attacker.getRandomAttack(attacker) ?: return
        attacker.attack(attacker, attack)
    }
}