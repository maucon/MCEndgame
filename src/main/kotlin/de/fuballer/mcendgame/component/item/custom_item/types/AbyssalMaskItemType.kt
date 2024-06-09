package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.component.item.attribute.data.RollableAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Helmet
import de.fuballer.mcendgame.util.PluginUtil

object AbyssalMaskItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("abyssal_mask")
    override val customName = "Abyssal Mask"
    override val equipment = Helmet.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableAttribute(AttributeType.NEGATIVE_EFFECT_IMMUNITY),
    )
}