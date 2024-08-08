package de.fuballer.mcendgame.component.item.custom_item

import de.fuballer.mcendgame.component.item.attribute.data.RollableCustomAttribute
import de.fuballer.mcendgame.component.item.custom_item.types.*
import de.fuballer.mcendgame.component.item.equipment.Equipment
import de.fuballer.mcendgame.technical.registry.Keyed
import de.fuballer.mcendgame.technical.registry.KeyedRegistry
import de.fuballer.mcendgame.util.ItemUtil
import de.fuballer.mcendgame.util.TextComponent
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setCustomAttributes
import de.fuballer.mcendgame.util.extension.ItemStackExtension.setCustomItemType
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

interface CustomItemType : Keyed {
    val customName: String
    val equipment: Equipment
    val usesEquipmentBaseStats: Boolean
    val attributes: List<RollableCustomAttribute>

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

        fun createItem(
            itemType: CustomItemType,
            percentageRoll: Double? = null
        ): ItemStack {
            val item = ItemStack(itemType.equipment.material)
            val itemMeta = item.itemMeta!!

            itemMeta.displayName(TextComponent.create(itemType.customName, NamedTextColor.GOLD))
            val customAttributes = itemType.attributes
                .map { it.roll(percentageRoll ?: Random.nextDouble()) }

            if (!itemType.usesEquipmentBaseStats) {
                itemMeta.attributeModifiers?.let {
                    it.forEach { attribute, _ -> itemMeta.removeAttributeModifier(attribute) }
                }
            }

            item.itemMeta = itemMeta

            item.setCustomItemType(itemType)
            item.setCustomAttributes(customAttributes)

            ItemUtil.updateAttributesAndLore(item)
            return item
        }
    }
}