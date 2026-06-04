package de.fuballer.mcendgame.main.component.item.custom.totem.item

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.IntBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.IntRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemItem
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemType

class TotemOfRimeItem(
    settings: Properties,
) : TotemItem(settings) {
    override val maxTier = 2
    override val type = TotemType.EFFECT

    override fun getCustomAttributes(tier: Int) =
        when (tier) {
            0 -> listOf(CustomAttribute(CustomAttributeTypes.SLOWNESS_ON_HIT, 0, listOf(IntRoll(IntBounds(1)), IntRoll(IntBounds(1)))))
            1 -> listOf(CustomAttribute(CustomAttributeTypes.SLOWNESS_ON_HIT, 0, listOf(IntRoll(IntBounds(1)), IntRoll(IntBounds(3)))))
            2 -> listOf(CustomAttribute(CustomAttributeTypes.SLOWNESS_ON_HIT, 0, listOf(IntRoll(IntBounds(2)), IntRoll(IntBounds(3)))))
            else -> listOf()
        }
}