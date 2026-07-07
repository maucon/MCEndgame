package de.fuballer.mcendgame.main.component.status_effect.verdant_echo

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory

private const val REGEN_INTERVAL = 20
private const val HEAL = 1f
private const val ADDITIONAL_HEAL_PER_AMPLIFIER = 0.5f

class VerdantEchoEffect : StatusEffect(StatusEffectCategory.BENEFICIAL, 1349140) {
    override fun applyUpdateEffect(entity: LivingEntity, amplifier: Int): Boolean {
        if (entity.health >= entity.maxHealth) return true
        val heal = HEAL + amplifier * ADDITIONAL_HEAL_PER_AMPLIFIER
        entity.heal(heal)
        return true
    }

    override fun canApplyUpdateEffect(duration: Int, amplifier: Int) = duration % REGEN_INTERVAL == 0
}