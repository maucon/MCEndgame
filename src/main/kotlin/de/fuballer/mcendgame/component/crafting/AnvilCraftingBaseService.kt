package de.fuballer.mcendgame.component.crafting

import de.fuballer.mcendgame.util.ItemUtil
import de.fuballer.mcendgame.util.SchedulingUtil
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryView
import org.bukkit.inventory.ItemStack

abstract class AnvilCraftingBaseService : Listener {
    protected open val repairCost = 1

    abstract fun isBaseValid(base: ItemStack): Boolean
    abstract fun isCraftingItemValid(craftingItem: ItemStack): Boolean
    abstract fun getResultPreview(base: ItemStack): ItemStack
    abstract fun getResult(base: ItemStack, craftingItem: ItemStack): ItemStack

    open fun cleanupInventory(inventory: Inventory) {
        inventory.setItem(0, null)
        inventory.setItem(2, null)
    }

    @EventHandler(ignoreCancelled = true)
    fun on(event: PrepareAnvilEvent) {
        val inventory = event.inventory
        if (!inventory.renameText.isNullOrEmpty()) return

        val base = inventory.getItem(0) ?: return
        if (!isBaseValid(base)) return

        val craftingItem = inventory.getItem(1) ?: return
        if (!isCraftingItemValid(craftingItem)) return

        val resultPreview = getResultPreview(base.clone())
        ItemUtil.updateAttributesAndLore(resultPreview)
        event.result = resultPreview

        SchedulingUtil.runTask {
            event.inventory.repairCost = repairCost

            event.inventory.viewers.forEach {
                it.setWindowProperty(InventoryView.Property.REPAIR_COST, repairCost)
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    open fun on(event: InventoryClickEvent) {
        val inventory = event.inventory
        if (inventory.type != InventoryType.ANVIL) return
        if (event.rawSlot != 2) return

        val base = inventory.getItem(0) ?: return
        if (!isBaseValid(base)) return

        val craftingItem = inventory.getItem(1) ?: return
        if (!isCraftingItemValid(craftingItem)) return

        val player = event.whoClicked as Player

        if (player.gameMode != GameMode.CREATIVE) {
            if (player.level < repairCost) return
            player.level -= repairCost
        }

        val result = getResult(base.clone(), craftingItem)
        ItemUtil.updateAttributesAndLore(result)

        playAnvilSound(result, player)

        when (event.action) {
            InventoryAction.PICKUP_ALL, InventoryAction.PICKUP_ONE, InventoryAction.PICKUP_HALF, InventoryAction.PICKUP_SOME ->
                player.setItemOnCursor(result)

            InventoryAction.MOVE_TO_OTHER_INVENTORY ->
                player.inventory.addItem(result)

            else -> return
        }

        cleanupInventory(inventory)
        decreaseCraftingItemStack(craftingItem, inventory)

        event.cancel()
    }

    private fun playAnvilSound(result: ItemStack, player: Player) {
        val sound = if (result.type == Material.AIR) Sound.ENTITY_ITEM_BREAK else Sound.BLOCK_ANVIL_USE
        player.world.playSound(player.location, sound, SoundCategory.PLAYERS, 1f, 1f)
    }

    private fun decreaseCraftingItemStack(craftingItem: ItemStack, inventory: Inventory) {
        craftingItem.amount -= 1
        inventory.setItem(1, craftingItem)
    }
}