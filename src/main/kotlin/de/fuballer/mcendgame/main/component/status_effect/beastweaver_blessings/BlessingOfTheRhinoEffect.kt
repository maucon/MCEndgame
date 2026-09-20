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

class BlessingOfTheRhinoEffect : MobEffect(MobEffectCategory.BENEFICIAL, 9835540) {
    companion object {
        val ATTRIBUTE_IDENTIFIER = IdentifierUtil.default("effect.blessing_of_the_rhino")

        fun getCustomAttributes(amplifier: Int) = listOf(
            CustomAttribute(
                CustomAttributeTypes.MORE_DAMAGE_TAKEN,
                0,
                DoubleRoll(DoubleBounds((amplifier + 1) * -0.08)),
            )
        )
    }

    init {
        addAttributeModifier(Attributes.ATTACK_SPEED, ATTRIBUTE_IDENTIFIER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    }
}