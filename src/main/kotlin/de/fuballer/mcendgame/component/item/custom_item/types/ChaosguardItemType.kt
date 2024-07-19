package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.component.item.attribute.data.RollableAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Chestplate
import de.fuballer.mcendgame.util.PluginUtil

object ChaosguardItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("chaosguard")
    override val customName = "Chaosguard"
    override val equipment = Chestplate.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableAttribute(AttributeType.ARMOR_TOUGHNESS, 2.0, 4.0),
        RollableAttribute(AttributeType.MAX_HEALTH, 1.0, 2.0),
        RollableAttribute(AttributeType.RANDOMIZED_DAMAGE_TAKEN, 1.4, 1.6),
    )
}