package de.fuballer.mcendgame.domain.item

import de.fuballer.mcendgame.domain.attribute.AttributeType
import de.fuballer.mcendgame.domain.attribute.RollableAttribute
import de.fuballer.mcendgame.domain.equipment.tool.Sword
import de.fuballer.mcendgame.util.PluginUtil

object TwinfireItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("twinfire")
    override val customName = "Twinfire"
    override val equipment = Sword.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableAttribute(AttributeType.TWINFIRE_DUAL_WIELD, 0.15, 0.25),
    )
}