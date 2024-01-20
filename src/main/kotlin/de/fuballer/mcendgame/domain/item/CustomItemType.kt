package de.fuballer.mcendgame.domain.item

import de.fuballer.mcendgame.domain.attribute.RollableAttribute
import de.fuballer.mcendgame.domain.equipment.Equipment
import de.fuballer.mcendgame.domain.technical.registry.Keyed
import de.fuballer.mcendgame.domain.technical.registry.KeyedRegistry

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
        }
    }
}