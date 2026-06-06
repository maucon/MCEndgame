package de.fuballer.mcendgame.main.component.item.custom.totem.item

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemItem
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemType

class TotemOfReachItem(
    settings: Properties,
) : TotemItem(settings) {
    override val maxTier = 2
    override val type = TotemType.ULTIMATE

    override fun getCustomAttributes(tier: Int) =
        when (tier) {
            0 -> listOf(
                CustomAttribute(VanillaAttributeTypes.INCREASED_ENTITY_INTERACTION_RANGE, 0, DoubleRoll(DoubleBounds(0.1))),
            )

            1 -> listOf(
                CustomAttribute(VanillaAttributeTypes.INCREASED_ENTITY_INTERACTION_RANGE, 0, DoubleRoll(DoubleBounds(0.15))),
                CustomAttribute(CustomAttributeTypes.INCREASED_DAMAGE, 0, DoubleRoll(DoubleBounds(0.1))),
            )

            2 -> listOf(
                CustomAttribute(VanillaAttributeTypes.INCREASED_ENTITY_INTERACTION_RANGE, 0, DoubleRoll(DoubleBounds(0.2))),
                CustomAttribute(CustomAttributeTypes.INCREASED_DAMAGE, 0, DoubleRoll(DoubleBounds(0.2))),
            )

            else -> listOf()
        }
}