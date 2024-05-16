package de.fuballer.mcendgame.component.crafting.transfiguration

import de.fuballer.mcendgame.component.crafting.AnvilCraftingBaseService
import de.fuballer.mcendgame.component.item.equipment.Equipment
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ItemUtil
import de.fuballer.mcendgame.util.extension.ItemStackExtension.getRolledAttributes
import de.fuballer.mcendgame.util.extension.ItemStackExtension.isTransfiguration
import org.bukkit.inventory.ItemStack

@Component
class TransfigurationService : AnvilCraftingBaseService() {
    override fun isBaseValid(base: ItemStack) =
        Equipment.existsByMaterial(base.type)
                && hasMultipleAttributes(base)

    override fun isCraftingItemValid(craftingItem: ItemStack) =
        craftingItem.isTransfiguration()

    override fun getResultPreview(base: ItemStack) = TransfigurationSettings.getPreviewItem()

    override fun getResult(base: ItemStack, craftingItem: ItemStack): ItemStack {
        val attributes = base.getRolledAttributes()?.toMutableList() ?: return base
        val attributesBounds = ItemUtil.getEquipmentAttributes(base)

        attributes.map { attribute ->
            val attributeBound = attributesBounds.firstOrNull { it.type == attribute.type }!!
            (attribute.roll - attributeBound.min) / (attributeBound.max - attributeBound.min)
        }
            .toMutableList()
            .shuffled()
            .zip(attributes)
            .forEach { (newPercentage, attribute) ->
                val bound = attributesBounds.firstOrNull { it.type == attribute.type }!!
                attribute.roll = newPercentage * (bound.max - bound.min) + bound.min
            }

        return base
    }

    private fun hasMultipleAttributes(item: ItemStack): Boolean {
        val attributes = item.getRolledAttributes() ?: return false
        return attributes.size >= 2
    }
}