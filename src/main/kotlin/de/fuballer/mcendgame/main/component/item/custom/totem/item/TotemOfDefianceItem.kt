package de.fuballer.mcendgame.main.component.item.custom.totem.item

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.IntBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.IntRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemItem
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemType

class TotemOfDefianceItem(
    settings: Properties,
) : TotemItem(settings) {
    override val maxTier = 2
    override val type = TotemType.EFFECT

    override fun getCustomAttributes(tier: Int) =
        when (tier) {
            0 -> listOf(CustomAttribute(CustomAttributeTypes.RESISTANCE_WHEN_LOW_HEALTH, 0, IntRoll(IntBounds(5))))
            1 -> listOf(CustomAttribute(CustomAttributeTypes.RESISTANCE_WHEN_LOW_HEALTH, 0, IntRoll(IntBounds(10))))
            2 -> listOf(CustomAttribute(CustomAttributeTypes.RESISTANCE_WHEN_LOW_HEALTH, 0, IntRoll(IntBounds(15))))
            else -> listOf()
        }
}