package de.fuballer.mcendgame.main.component.item.custom.totem.item

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemItem
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemType

class TotemOfRestorationItem(
    settings: Properties,
) : TotemItem(settings) {
    override val maxTier = 2
    override val type = TotemType.ULTIMATE

    override fun getCustomAttributes(tier: Int) =
        when (tier) {
            0 -> listOf(
                CustomAttribute(CustomAttributeTypes.MORE_HEALING, 0, DoubleRoll(DoubleBounds(0.1))),
                CustomAttribute(CustomAttributeTypes.CHANCE_TO_HEAL_MORE, 0, listOf(DoubleRoll(DoubleBounds(0.2)), DoubleRoll(DoubleBounds(0.5)))),
            )

            1 -> listOf(
                CustomAttribute(CustomAttributeTypes.MORE_HEALING, 0, DoubleRoll(DoubleBounds(0.15))),
                CustomAttribute(CustomAttributeTypes.CHANCE_TO_HEAL_MORE, 0, listOf(DoubleRoll(DoubleBounds(0.35)), DoubleRoll(DoubleBounds(0.5)))),
            )

            2 -> listOf(
                CustomAttribute(CustomAttributeTypes.MORE_HEALING, 0, DoubleRoll(DoubleBounds(0.2))),
                CustomAttribute(CustomAttributeTypes.CHANCE_TO_HEAL_MORE, 0, listOf(DoubleRoll(DoubleBounds(0.5)), DoubleRoll(DoubleBounds(0.5)))),
            )

            else -> listOf()
        }
}