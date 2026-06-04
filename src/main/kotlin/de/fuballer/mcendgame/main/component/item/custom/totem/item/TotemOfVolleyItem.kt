package de.fuballer.mcendgame.main.component.item.custom.totem.item

import de.fuballer.mcendgame.main.component.custom_attribute.data.*
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemItem
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemType

class TotemOfVolleyItem(
    settings: Properties,
) : TotemItem(settings) {
    override val maxTier = 2
    override val type = TotemType.ULTIMATE

    override fun getCustomAttributes(tier: Int) =
        when (tier) {
            0 -> listOf(
                CustomAttribute(CustomAttributeTypes.ADDITIONAL_ARROWS, 0, IntRoll(IntBounds(2))),
            )

            1 -> listOf(
                CustomAttribute(CustomAttributeTypes.ADDITIONAL_ARROWS, 0, IntRoll(IntBounds(2))),
                CustomAttribute(CustomAttributeTypes.INCREASED_PROJECTILE_DAMAGE, 0, DoubleRoll(DoubleBounds(0.2))),
            )

            2 -> listOf(
                CustomAttribute(CustomAttributeTypes.ADDITIONAL_ARROWS, 0, IntRoll(IntBounds(4))),
                CustomAttribute(CustomAttributeTypes.INCREASED_PROJECTILE_DAMAGE, 0, DoubleRoll(DoubleBounds(0.25))),
            )

            else -> listOf()
        }
}