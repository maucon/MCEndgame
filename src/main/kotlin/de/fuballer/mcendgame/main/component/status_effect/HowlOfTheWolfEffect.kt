package de.fuballer.mcendgame.main.component.status_effect

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.util.ColorUtil
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory

class HowlOfTheWolfEffect : MobEffect(MobEffectCategory.BENEFICIAL, ColorUtil.rgbaToInt(95, 141, 72, 255)) {
    companion object {
        val ATTRIBUTE_IDENTIFIER = IdentifierUtil.default("effect.howl_of_the_wolf")

        fun getCustomAttributes(amplifier: Int) = listOf(
            CustomAttribute(
                CustomAttributeTypes.INCREASED_COMPANION_DAMAGE,
                0,
                DoubleRoll(DoubleBounds(0.2 + 0.1 * amplifier)),
            )
        )
    }
}