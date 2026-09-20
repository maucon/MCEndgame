package de.fuballer.mcendgame.main.component.status_effect.beastweaver_blessings

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory

class BlessingOfTheEagleEffect : MobEffect(MobEffectCategory.BENEFICIAL, 9835540) {
    companion object {
        val ATTRIBUTE_IDENTIFIER = IdentifierUtil.default("effect.blessing_of_the_eagle")

        fun getCustomAttributes(amplifier: Int) = listOf(
            CustomAttribute(
                CustomAttributeTypes.CRITICAL_DAMAGE_MULTIPLIER,
                0,
                DoubleRoll(DoubleBounds(amplifier * 0.2)),
            )
        )
    }
}