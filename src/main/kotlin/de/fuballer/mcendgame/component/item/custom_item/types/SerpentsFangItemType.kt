package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.component.item.attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.tool.Sword
import de.fuballer.mcendgame.util.PluginUtil

object SerpentsFangItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("serpents_fang")
    override val customName = "Serpent's Fang"
    override val equipment = Sword.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableCustomAttribute(AttributeType.CRITICAL_DAMAGE, 0.1, 0.3),
        RollableCustomAttribute(AttributeType.CRITICAL_EXECUTE, 0.05, 0.12),
    )
}