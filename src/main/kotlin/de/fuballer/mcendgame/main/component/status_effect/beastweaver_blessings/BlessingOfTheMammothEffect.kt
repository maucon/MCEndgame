package de.fuballer.mcendgame.main.component.status_effect.beastweaver_blessings

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes

class BlessingOfTheMammothEffect : MobEffect(MobEffectCategory.BENEFICIAL, 9835540) {
    companion object {
        val ATTRIBUTE_IDENTIFIER = IdentifierUtil.default("effect.blessing_of_the_mammoth")
    }

    init {
        addAttributeModifier(Attributes.MAX_HEALTH, ATTRIBUTE_IDENTIFIER, 3.0, AttributeModifier.Operation.ADD_VALUE)
        addAttributeModifier(Attributes.SCALE, ATTRIBUTE_IDENTIFIER, 0.08, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
    }
}