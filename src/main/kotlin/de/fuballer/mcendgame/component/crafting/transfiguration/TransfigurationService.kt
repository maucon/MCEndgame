package de.fuballer.mcendgame.component.crafting.transfiguration

import de.fuballer.mcendgame.component.crafting.AnvilCraftingBaseService
import de.fuballer.mcendgame.component.item.equipment.Equipment
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getCustomAttributes
import de.fuballer.mcendgame.util.extension.ItemStackExtension.isTransfiguration
import org.bukkit.inventory.ItemStack

@Component
class TransfigurationService : AnvilCraftingBaseService() {
    override fun isBaseValid(base: ItemStack) =
        Equipment.existsByMaterial(base.type)
                && hasMultipleAttributes(base)

    override fun isCraftingItemValid(craftingItem: ItemStack) = craftingItem.isTransfiguration()

    override fun getResultPreview(base: ItemStack) = base

    override fun getResult(base: ItemStack, craftingItem: ItemStack): ItemStack { // FIXME not working?
//        val attributes = base.getCustomAttributes()?.toMutableList() ?: return base
//        val attributesBounds = ItemUtil.getEquipmentAttributes(base)
//
//        // TODO filter out not rollable attributes
//        // attributeBounds.min == attributeBounds.max
//
//        attributes.map { attribute ->
//            val attributeBound = attributesBounds.firstOrNull { it.type == attribute.type }!!
//            (attribute.roll - attributeBound.min) / (attributeBound.max - attributeBound.min)
//        }
//            .toMutableList()
//            .shuffled()
//            .zip(attributes)
//            .forEach { (newPercentage, attribute) ->
//                val bound = attributesBounds.firstOrNull { it.type == attribute.type }!!
//                attribute.roll = newPercentage * (bound.max - bound.min) + bound.min
//            }

        return base
    }

    private fun hasMultipleAttributes(item: ItemStack): Boolean {
        val attributes = item.getCustomAttributes() ?: return false
        return attributes.size >= 2
    }
}