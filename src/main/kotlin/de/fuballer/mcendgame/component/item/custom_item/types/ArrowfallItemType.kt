package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.attribute.AttributeType
import de.fuballer.mcendgame.component.attribute.RollableAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Helmet
import de.fuballer.mcendgame.util.PluginUtil

object ArrowfallItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("arrowfall")
    override val customName = "Arrowfall"
    override val equipment = Helmet.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableAttribute(AttributeType.ADDITIONAL_ARROWS, 0.25, 0.75),
    )
}