package de.fuballer.mcendgame.main.component.status_effect.verdant_echo

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity

private const val REGEN_INTERVAL = 20
private const val HEAL = 1f
private const val ADDITIONAL_HEAL_PER_AMPLIFIER = 0.5f

class VerdantEchoEffect : MobEffect(MobEffectCategory.BENEFICIAL, 1349140) {
    override fun applyEffectTick(world: ServerLevel, entity: LivingEntity, amplifier: Int): Boolean {
        if (entity.health >= entity.maxHealth) return true
        val heal = HEAL + amplifier * ADDITIONAL_HEAL_PER_AMPLIFIER
        entity.heal(heal)
        return true
    }

    override fun shouldApplyEffectTickThisTick(duration: Int, amplifier: Int) = duration % REGEN_INTERVAL == 0
}