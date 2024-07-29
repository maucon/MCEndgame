package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.data.DoubleBounds
import de.fuballer.mcendgame.component.item.attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Helmet
import de.fuballer.mcendgame.util.PluginUtil

object AbyssalMaskItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("abyssal_mask")
    override val customName = "Abyssal Mask"
    override val equipment = Helmet.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableCustomAttribute(CustomAttributeTypes.NEGATIVE_EFFECT_IMMUNITY),
        RollableCustomAttribute(CustomAttributeTypes.DODGE_CHANCE, DoubleBounds(0.05, 0.15)),
        RollableCustomAttribute(CustomAttributeTypes.REDUCED_DAMAGE_TAKEN, DoubleBounds(0.03, 0.05)),
    )
}