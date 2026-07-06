package de.fuballer.mcendgame.main.component.status_effect.molten_roar

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory

class MoltenRoarEffect : StatusEffect(StatusEffectCategory.BENEFICIAL, 9835540) {
    companion object {
        val ATTRIBUTE_IDENTIFIER = IdentifierUtil.default("effect.molten_roar")
    }

    init {
        addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, ATTRIBUTE_IDENTIFIER, 0.05, EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    }
}