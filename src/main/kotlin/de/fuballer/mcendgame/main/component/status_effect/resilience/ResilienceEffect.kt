package de.fuballer.mcendgame.main.component.status_effect.resilience

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory
import net.minecraft.server.world.ServerWorld

class ResilienceEffect : StatusEffect(StatusEffectCategory.BENEFICIAL, 1349140) {
    companion object {
        val ATTRIBUTE_IDENTIFIER = IdentifierUtil.default("effect.resilience")
    }

    init {
        addAttributeModifier(EntityAttributes.GENERIC_SCALE, ATTRIBUTE_IDENTIFIER, 0.015, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    }

    override fun applyUpdateEffect(world: ServerWorld?, entity: LivingEntity, amplifier: Int): Boolean {
        if (entity.age % 20 != 0) return true

        if (entity.health < entity.maxHealth) {
            entity.heal(0.1F * (amplifier + 1))
        }

        return true
    }

    override fun canApplyUpdateEffect(duration: Int, amplifier: Int) = true
}