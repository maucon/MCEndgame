package de.fuballer.mcendgame.main.component.item.equipment.data

import de.fuballer.mcendgame.main.component.custom_attribute.data.AttributeBounds

data class AttributeTierData(
    val tier: Int,
    val bounds: List<AttributeBounds<*>>,
) {
    constructor(
        tier: Int,
        vararg bounds: AttributeBounds<*>
    ) : this(tier, bounds.toList())
}