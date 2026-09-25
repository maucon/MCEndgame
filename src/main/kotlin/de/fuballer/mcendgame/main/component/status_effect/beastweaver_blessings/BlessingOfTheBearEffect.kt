package de.fuballer.mcendgame.main.component.status_effect.beastweaver_blessings

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes

class BlessingOfTheBearEffect : MobEffect(MobEffectCategory.BENEFICIAL, BeastweaverBlessingEffect.PARTICLE_COLOR), BeastweaverBlessingEffect {
    companion object {
        val ATTRIBUTE_IDENTIFIER = IdentifierUtil.default("effect.blessing_of_the_bear")
    }

    init {
        addAttributeModifier(Attributes.ATTACK_DAMAGE, ATTRIBUTE_IDENTIFIER, 2.5, AttributeModifier.Operation.ADD_VALUE)
    }
}