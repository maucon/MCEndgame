package de.fuballer.mcendgame.main.component.item.custom.totem.item

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.VanillaAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemItem
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemType

class TotemOfVanguardItem(
    settings: Properties,
) : TotemItem(settings) {
    override val maxTier = 2
    override val type = TotemType.BASIC

    override fun getCustomAttributes(tier: Int) =
        when (tier) {
            0 -> listOf(CustomAttribute(VanillaAttributeTypes.ARMOR_TOUGHNESS, 0, DoubleRoll(DoubleBounds(0.8))))
            1 -> listOf(CustomAttribute(VanillaAttributeTypes.ARMOR_TOUGHNESS, 0, DoubleRoll(DoubleBounds(1.4))))
            2 -> listOf(CustomAttribute(VanillaAttributeTypes.ARMOR_TOUGHNESS, 0, DoubleRoll(DoubleBounds(2.0))))
            else -> listOf()
        }
}