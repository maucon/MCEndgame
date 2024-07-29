package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.VanillaAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.data.DoubleBounds
import de.fuballer.mcendgame.component.item.attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Chestplate
import de.fuballer.mcendgame.util.PluginUtil

object VitalitySurgeItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("vitality_surge")
    override val customName = "Vitality Surge"
    override val equipment = Chestplate.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableCustomAttribute(CustomAttributeTypes.ABSORPTION_ON_HIGH_DAMAGE_TAKEN, DoubleBounds(4.0, 6.0)),
        RollableCustomAttribute(VanillaAttributeTypes.ARMOR_TOUGHNESS, DoubleBounds(-3.0, -1.5)),
        RollableCustomAttribute(VanillaAttributeTypes.MAX_HEALTH, DoubleBounds(1.0, 3.0)),
    )
}