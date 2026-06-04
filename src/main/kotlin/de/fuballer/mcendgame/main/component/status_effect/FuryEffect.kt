package de.fuballer.mcendgame.main.component.status_effect

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes

class FuryEffect : MobEffect(MobEffectCategory.BENEFICIAL, 9835540) {
    companion object {
        val ATTRIBUTE_IDENTIFIER = IdentifierUtil.default("effect.fury")
    }

    init {
        addAttributeModifier(Attributes.ATTACK_DAMAGE, ATTRIBUTE_IDENTIFIER, 0.02, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
        addAttributeModifier(Attributes.ATTACK_SPEED, ATTRIBUTE_IDENTIFIER, 0.02, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
        addAttributeModifier(Attributes.MOVEMENT_SPEED, ATTRIBUTE_IDENTIFIER, 0.015, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    }
}