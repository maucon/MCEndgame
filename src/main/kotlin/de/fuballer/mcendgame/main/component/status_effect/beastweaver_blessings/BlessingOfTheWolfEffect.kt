package de.fuballer.mcendgame.main.component.status_effect.beastweaver_blessings

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes

class BlessingOfTheWolfEffect : MobEffect(MobEffectCategory.BENEFICIAL, BeastweaverBlessingEffect.PARTICLE_COLOR), BeastweaverBlessingEffect {
    companion object {
        val ATTRIBUTE_IDENTIFIER = IdentifierUtil.default("effect.blessing_of_the_wolf")

        fun getCustomAttributes(amplifier: Int) = listOf(
            CustomAttribute(
                CustomAttributeTypes.INCREASED_COMPANION_DAMAGE,
                0,
                DoubleRoll(DoubleBounds((amplifier + 1) * 0.12)),
            )
        )
    }

    init {
        addAttributeModifier(Attributes.ATTACK_SPEED, ATTRIBUTE_IDENTIFIER, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
    }
}