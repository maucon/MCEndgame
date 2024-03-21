package de.fuballer.mcendgame.component.crafting

import de.fuballer.mcendgame.util.ItemUtil
import de.fuballer.mcendgame.util.SchedulingUtil
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
import org.bukkit.inventory.InventoryView
import org.bukkit.inventory.ItemStack

abstract class AnvilCraftingBaseService : Listener {
    protected open val repairCost = 1

    abstract fun isBaseValid(base: ItemStack): Boolean
    abstract fun isCraftingItemValid(craftingItem: ItemStack): Boolean
    abstract fun getResultPreview(base: ItemStack): ItemStack
    abstract fun updateResult(result: ItemStack, craftingItem: ItemStack)

    @EventHandler(ignoreCancelled = true)
    fun on(event: PrepareAnvilEvent) {
        val inventory = event.inventory

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
    fun on(event: InventoryClickEvent) {
        val inventory = event.inventory
        if (inventory.type != InventoryType.ANVIL) return
        if (event.rawSlot != 2) return

        val base = inventory.getItem(0) ?: return
        if (!isBaseValid(base)) return

        val craftingItem = inventory.getItem(1) ?: return
        if (!isCraftingItemValid(craftingItem)) return

        val player = event.whoClicked as Player

        if (player.gameMode != GameMode.CREATIVE) {
            if (player.level < 1) return
            player.level -= 1
        }

        val result = inventory.getItem(2) ?: return
        updateResult(result, craftingItem)
        ItemUtil.updateAttributesAndLore(result)

        val sound = if (result.type == Material.AIR) Sound.ENTITY_ITEM_BREAK else Sound.BLOCK_ANVIL_USE
        player.world.playSound(player.location, sound, SoundCategory.PLAYERS, 1f, 1f)

        when (event.action) {
            InventoryAction.PICKUP_ALL, InventoryAction.PICKUP_ONE, InventoryAction.PICKUP_HALF, InventoryAction.PICKUP_SOME ->
                player.setItemOnCursor(result)

            InventoryAction.MOVE_TO_OTHER_INVENTORY ->
                player.inventory.addItem(result)

            else -> return
        }

        inventory.setItem(0, null)
        inventory.setItem(2, null)

        val craftingItemStack = inventory.getItem(1) ?: return
        craftingItemStack.amount -= 1
        inventory.setItem(1, craftingItemStack)

        event.isCancelled = true
    }
}