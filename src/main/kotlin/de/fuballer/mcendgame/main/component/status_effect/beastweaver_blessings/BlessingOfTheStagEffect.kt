package de.fuballer.mcendgame.main.component.status_effect.beastweaver_blessings

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes

class BlessingOfTheStagEffect : MobEffect(MobEffectCategory.BENEFICIAL, 9835540) {
    companion object {
        val ATTRIBUTE_IDENTIFIER = IdentifierUtil.default("effect.blessing_of_the_stag")
    }

    init {
        addAttributeModifier(Attributes.MOVEMENT_SPEED, ATTRIBUTE_IDENTIFIER, 0.15, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
        addAttributeModifier(Attributes.JUMP_STRENGTH, ATTRIBUTE_IDENTIFIER, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
    }
}