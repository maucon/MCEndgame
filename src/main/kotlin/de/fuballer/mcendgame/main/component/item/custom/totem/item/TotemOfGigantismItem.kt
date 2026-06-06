package de.fuballer.mcendgame.main.component.item.custom.totem.item

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemItem
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemType

class TotemOfGigantismItem(
    settings: Properties,
) : TotemItem(settings) {
    override val maxTier = 2
    override val type = TotemType.ULTIMATE

    override fun getCustomAttributes(tier: Int) =
        when (tier) {
            0 -> listOf(
                CustomAttribute(VanillaAttributeTypes.MORE_MAX_HEALTH, 0, DoubleRoll(DoubleBounds(0.2))),
                CustomAttribute(VanillaAttributeTypes.INCREASED_SCALE, 0, DoubleRoll(DoubleBounds(0.05))),
            )

            1 -> listOf(
                CustomAttribute(VanillaAttributeTypes.MORE_MAX_HEALTH, 0, DoubleRoll(DoubleBounds(0.25))),
                CustomAttribute(CustomAttributeTypes.MORE_DAMAGE_TAKEN, 0, DoubleRoll(DoubleBounds(-0.05))),
                CustomAttribute(VanillaAttributeTypes.INCREASED_SCALE, 0, DoubleRoll(DoubleBounds(0.1))),
            )

            2 -> listOf(
                CustomAttribute(VanillaAttributeTypes.MORE_MAX_HEALTH, 0, DoubleRoll(DoubleBounds(0.3))),
                CustomAttribute(CustomAttributeTypes.MORE_DAMAGE_TAKEN, 0, DoubleRoll(DoubleBounds(-0.1))),
                CustomAttribute(VanillaAttributeTypes.INCREASED_SCALE, 0, DoubleRoll(DoubleBounds(0.15))),
            )

            else -> listOf()
        }
}