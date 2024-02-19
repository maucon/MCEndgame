package de.fuballer.mcendgame.component.item.custom_item

import de.fuballer.mcendgame.component.attribute.RollableAttribute
import de.fuballer.mcendgame.component.item.custom_item.types.*
import de.fuballer.mcendgame.component.item.equipment.Equipment
import de.fuballer.mcendgame.technical.registry.Keyed
import de.fuballer.mcendgame.technical.registry.KeyedRegistry

interface CustomItemType : Keyed {
    val customName: String
    val equipment: Equipment
    val usesEquipmentBaseStats: Boolean
    val attributes: List<RollableAttribute>

    companion object {
        val REGISTRY = KeyedRegistry<CustomItemType>().also {
            it.register(ArcheryAnnexItemType)
            it.register(GeistergaloschenItemType)
            it.register(ShrinkshadowItemType)
            it.register(TitansEmbraceItemType)
            it.register(TwinfireItemType)
            it.register(VitalitySurgeItemType)
        }
    }
}