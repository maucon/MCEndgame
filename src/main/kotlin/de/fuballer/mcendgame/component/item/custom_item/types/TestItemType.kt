package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.data.DoubleBounds
import de.fuballer.mcendgame.component.item.attribute.data.IntBounds
import de.fuballer.mcendgame.component.item.attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.component.item.attribute.data.StringBounds
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Helmet
import de.fuballer.mcendgame.util.PluginUtil

object TestItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("test")
    override val customName = "Test"
    override val equipment = Helmet.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableCustomAttribute(CustomAttributeTypes.TEST, DoubleBounds(0.05, 0.15), StringBounds("burger", "skeleton", "prismarine", "otto", "fornite"), IntBounds(1, 100)),
        RollableCustomAttribute(CustomAttributeTypes.ADDITIONAL_ARROWS, IntBounds(10, 100), DoubleBounds(0.1, 1.5)),
    )
}