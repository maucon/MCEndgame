package de.fuballer.mcendgame.main.component.status_effect

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes

private const val BASE_MORE_DAMAGE = 0.1
private const val MORE_DAMAGE_PER_AMPLIFIER = 0.05

class MoltenRoarEffect : MobEffect(MobEffectCategory.BENEFICIAL, 9835540) {
    companion object {
        val ATTRIBUTE_IDENTIFIER = IdentifierUtil.default("effect.molten_roar")

        fun getCustomAttributes(amplifier: Int) = listOf(
            CustomAttribute(
                CustomAttributeTypes.MORE_DAMAGE,
                0,
                DoubleRoll(DoubleBounds(BASE_MORE_DAMAGE + (amplifier * MORE_DAMAGE_PER_AMPLIFIER))),
            )
        )
    }

    init {
        addAttributeModifier(Attributes.ATTACK_SPEED, ATTRIBUTE_IDENTIFIER, 0.05, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    }
}