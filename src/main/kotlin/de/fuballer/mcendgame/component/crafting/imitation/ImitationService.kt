package de.fuballer.mcendgame.component.crafting.imitation

import de.fuballer.mcendgame.component.crafting.AnvilCraftingBaseService
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.ItemStackExtension.isImitation
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

@Component
class ImitationService : AnvilCraftingBaseService() {
    override val repairCost = 64

    override fun isBaseValid(base: ItemStack) = true

    override fun isCraftingItemValid(craftingItem: ItemStack) = craftingItem.isImitation()

    override fun getResultPreview(base: ItemStack) = base

    override fun getResult(base: ItemStack, craftingItem: ItemStack) = base

    @EventHandler(ignoreCancelled = true)
    override fun on(event: InventoryClickEvent) {
        super.on(event)

        val inventory = event.inventory
        val base = inventory.getItem(0) ?: return

        inventory.setItem(0, base)
    }
}