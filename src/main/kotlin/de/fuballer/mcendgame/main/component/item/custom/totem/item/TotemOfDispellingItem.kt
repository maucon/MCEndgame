package de.fuballer.mcendgame.main.component.item.custom.totem.item

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemItem
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemType

class TotemOfDispellingItem(
    settings: Properties,
) : TotemItem(settings) {
    override val maxTier = 2
    override val type = TotemType.BASIC

    override fun getCustomAttributes(tier: Int) =
        when (tier) {
            0 -> listOf(CustomAttribute(CustomAttributeTypes.SPELL_RESISTANCE, 0, DoubleRoll(DoubleBounds(0.04))))
            1 -> listOf(CustomAttribute(CustomAttributeTypes.SPELL_RESISTANCE, 0, DoubleRoll(DoubleBounds(0.07))))
            2 -> listOf(CustomAttribute(CustomAttributeTypes.SPELL_RESISTANCE, 0, DoubleRoll(DoubleBounds(0.1))))
            else -> listOf()
        }
}