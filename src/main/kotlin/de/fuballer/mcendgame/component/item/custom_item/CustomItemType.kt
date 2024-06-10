package de.fuballer.mcendgame.component.item.custom_item

import de.fuballer.mcendgame.component.item.attribute.data.RollableAttribute
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
            it.register(AbyssalMaskItemType)
            it.register(ArcheryAnnexItemType)
            it.register(ArrowfallItemType)
            it.register(BitterfrostItemType)
            it.register(BloodlustItemType)
            it.register(ChaosguardItemType)
            it.register(FatesplitterItemType)
            it.register(GalestrideItemType)
            it.register(GeistergaloschenItemType)
            it.register(HeadhuntersHaremType)
            it.register(LifewardAegisItemType)
            it.register(MoonshadowItemType)
            it.register(SerpentsFangItemType)
            it.register(ShrinkshadowItemType)
            it.register(StonewardItemType)
            it.register(StormfeatherItemType)
            it.register(TitansEmbraceItemType)
            it.register(TwinfireItemType)
            it.register(TyrantsReachItemType)
            it.register(VitalitySurgeItemType)
            it.register(CrownOfConflictItemType)
            it.register(WingedFlightItemType)
        }
    }
}