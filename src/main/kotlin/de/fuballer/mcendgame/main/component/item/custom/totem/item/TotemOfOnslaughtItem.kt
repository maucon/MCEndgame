package de.fuballer.mcendgame.main.component.item.custom.totem.item

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.IntBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.IntRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemItem
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemType

class TotemOfOnslaughtItem(
    settings: Properties,
) : TotemItem(settings) {
    override val maxTier = 2
    override val type = TotemType.EFFECT

    override fun getCustomAttributes(tier: Int) =
        when (tier) {
            0 -> listOf(
                CustomAttribute(CustomAttributeTypes.SPEED_ON_KILL, 0, listOf(IntRoll(IntBounds(2)), IntRoll(IntBounds(4)))),
            )

            1 -> listOf(
                CustomAttribute(CustomAttributeTypes.SPEED_ON_KILL, 0, listOf(IntRoll(IntBounds(2)), IntRoll(IntBounds(4)))),
                CustomAttribute(CustomAttributeTypes.STRENGTH_ON_KILL, 0, listOf(IntRoll(IntBounds(2)), IntRoll(IntBounds(4)))),
            )

            2 -> listOf(
                CustomAttribute(CustomAttributeTypes.SPEED_ON_KILL, 0, listOf(IntRoll(IntBounds(2)), IntRoll(IntBounds(4)))),
                CustomAttribute(CustomAttributeTypes.STRENGTH_ON_KILL, 0, listOf(IntRoll(IntBounds(2)), IntRoll(IntBounds(4)))),
                CustomAttribute(CustomAttributeTypes.HASTE_ON_KILL, 0, listOf(IntRoll(IntBounds(2)), IntRoll(IntBounds(4)))),
            )

            else -> listOf()
        }
}