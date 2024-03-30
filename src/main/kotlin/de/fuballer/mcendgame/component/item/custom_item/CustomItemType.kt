package de.fuballer.mcendgame.component.item.custom_item

import de.fuballer.mcendgame.component.item.attribute.RollableAttribute
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
            it.register(ArrowfallItemType)
            it.register(BitterfrostItemType)
            it.register(FatesplitterItemType)
            it.register(GeistergaloschenItemType)
            it.register(HeadhuntersHaremType)
            it.register(LifewardAegisItemType)
            it.register(ShrinkshadowItemType)
            it.register(StormfeatherItemType)
            it.register(TitansEmbraceItemType)
            it.register(TwinfireItemType)
            it.register(VitalitySurgeItemType)
        }
    }
}