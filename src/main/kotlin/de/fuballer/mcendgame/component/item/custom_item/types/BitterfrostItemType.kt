package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.VanillaAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.data.DoubleBounds
import de.fuballer.mcendgame.component.item.attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.tool.Sword
import de.fuballer.mcendgame.util.PluginUtil

object BitterfrostItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("bitterfrost")
    override val customName = "Bitterfrost"
    override val equipment = Sword.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableCustomAttribute(CustomAttributeTypes.SLOW_ON_HIT, DoubleBounds(2.5, 5.0)),
        RollableCustomAttribute(CustomAttributeTypes.TAUNT, DoubleBounds(0.25, 0.75)),
        RollableCustomAttribute(VanillaAttributeTypes.ATTACK_SPEED, DoubleBounds(-0.4, -0.2)),
    )
}