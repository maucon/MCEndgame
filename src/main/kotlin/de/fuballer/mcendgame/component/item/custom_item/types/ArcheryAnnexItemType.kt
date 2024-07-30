package de.fuballer.mcendgame.component.item.custom_item.types

import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.data.DoubleBounds
import de.fuballer.mcendgame.component.item.attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.component.item.equipment.armor.Helmet
import de.fuballer.mcendgame.util.PluginUtil

object ArcheryAnnexItemType : CustomItemType {
    override val key = PluginUtil.createNamespacedKey("archery_annex")
    override val customName = "Archery Annex"
    override val equipment = Helmet.NETHERITE
    override val usesEquipmentBaseStats = true
    override val attributes = listOf(
        RollableCustomAttribute(CustomAttributeTypes.INCREASED_PROJECTILE_DAMAGE, DoubleBounds(0.25, 0.5)),
        RollableCustomAttribute(CustomAttributeTypes.REDUCED_PROJECTILE_DAMAGE_TAKEN, DoubleBounds(0.05, 0.1)),
        RollableCustomAttribute(CustomAttributeTypes.DISABLE_MELEE),
    )
}