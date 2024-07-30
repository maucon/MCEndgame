package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.data.DoubleBounds
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
        RollableCustomAttribute(CustomAttributeTypes.CRITICAL_DAMAGE, DoubleBounds(0.1, 0.35)),
        RollableCustomAttribute(CustomAttributeTypes.CRITICAL_EXECUTE, DoubleBounds(0.15, 0.20)),
    )
}