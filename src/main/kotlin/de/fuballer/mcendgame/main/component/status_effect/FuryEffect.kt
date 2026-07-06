package de.fuballer.mcendgame.main.component.status_effect

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory

class FuryEffect : StatusEffect(StatusEffectCategory.BENEFICIAL, 9835540) {
    companion object {
        val ATTRIBUTE_IDENTIFIER = IdentifierUtil.default("effect.fury")
    }

    init {
        addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, ATTRIBUTE_IDENTIFIER, 0.02, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
        addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, ATTRIBUTE_IDENTIFIER, 0.02, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
        addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, ATTRIBUTE_IDENTIFIER, 0.015, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    }
}