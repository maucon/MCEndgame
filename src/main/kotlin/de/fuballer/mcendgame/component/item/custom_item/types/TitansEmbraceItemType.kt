package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.VanillaAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.data.DoubleBounds
import de.fuballer.mcendgame.component.item.attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Chestplate
import de.fuballer.mcendgame.util.PluginUtil

object TitansEmbraceItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("titans_embrace")
    override val customName = "Titan's Embrace"
    override val equipment = Chestplate.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableCustomAttribute(VanillaAttributeTypes.ARMOR, DoubleBounds(-5.0, -2.0)),
        RollableCustomAttribute(VanillaAttributeTypes.MAX_HEALTH, DoubleBounds(3.0, 6.0)),
        RollableCustomAttribute(VanillaAttributeTypes.MAX_HEALTH_INCREASE, DoubleBounds(0.1, 0.2)),
        RollableCustomAttribute(CustomAttributeTypes.REGEN_ON_DAMAGE_TAKEN, DoubleBounds(5.0, 8.0)),
        RollableCustomAttribute(VanillaAttributeTypes.SIZE_INCREASE, DoubleBounds(0.1, 0.2)),
    )
}