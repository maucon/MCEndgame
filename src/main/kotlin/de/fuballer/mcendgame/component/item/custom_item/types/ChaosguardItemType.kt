package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.VanillaAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.data.DoubleBounds
import de.fuballer.mcendgame.component.item.attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Chestplate
import de.fuballer.mcendgame.util.PluginUtil

object ChaosguardItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("chaosguard")
    override val customName = "Chaosguard"
    override val equipment = Chestplate.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableCustomAttribute(VanillaAttributeTypes.ARMOR_TOUGHNESS, DoubleBounds(2.0, 4.0)),
        RollableCustomAttribute(VanillaAttributeTypes.MAX_HEALTH, DoubleBounds(1.0, 2.0)),
        RollableCustomAttribute(CustomAttributeTypes.RANDOMIZED_DAMAGE_TAKEN, DoubleBounds(1.4, 1.6)),
    )
}