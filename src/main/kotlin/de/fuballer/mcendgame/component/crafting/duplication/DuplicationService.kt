package de.fuballer.mcendgame.component.crafting.duplication

import de.fuballer.mcendgame.component.crafting.AnvilCraftingBaseService
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.ItemStackExtension.isDuplicationCraftingItem
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

@Component
class DuplicationService : AnvilCraftingBaseService() {
    override fun isBaseValid(base: ItemStack) = base.amount == 1

    override fun isCraftingItemValid(craftingItem: ItemStack) = craftingItem.isDuplicationCraftingItem()

    override fun getResultPreview(base: ItemStack) = base

    override fun getResult(base: ItemStack, craftingItem: ItemStack) = base

    override fun cleanupInventory(inventory: Inventory) {
        inventory.setItem(2, null)
    }
}