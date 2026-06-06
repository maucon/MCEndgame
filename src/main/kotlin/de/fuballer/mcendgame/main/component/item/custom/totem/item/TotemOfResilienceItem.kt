package de.fuballer.mcendgame.main.component.item.custom.totem.item

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemItem
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemType

class TotemOfResilienceItem(
    settings: Properties,
) : TotemItem(settings) {
    override val maxTier = 0
    override val type = TotemType.EFFECT

    override fun getCustomAttributes(tier: Int) =
        if (tier != 0) listOf() else listOf(CustomAttribute(CustomAttributeTypes.RESILIENCE_ON_DAMAGE_TAKEN, 0))
}