package de.fuballer.mcendgame.main.component.status_effect.molten_roar

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes

class MoltenRoarEffect : MobEffect(MobEffectCategory.BENEFICIAL, 9835540) {
    companion object {
        val ATTRIBUTE_IDENTIFIER = IdentifierUtil.default("effect.molten_roar")
    }

    init {
        addAttributeModifier(Attributes.ATTACK_SPEED, ATTRIBUTE_IDENTIFIER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    }
}