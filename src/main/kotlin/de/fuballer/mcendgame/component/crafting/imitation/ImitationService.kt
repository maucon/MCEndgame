package de.fuballer.mcendgame.component.crafting.imitation

import de.fuballer.mcendgame.component.crafting.AnvilCraftingBaseService
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ItemUtil
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import de.fuballer.mcendgame.util.extension.ItemStackExtension.isImitation
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
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

        val result = getResult(base.clone(), craftingItem)
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

        inventory.setItem(2, null)

        val craftingItemStack = inventory.getItem(1) ?: return
        craftingItemStack.amount -= 1
        inventory.setItem(1, craftingItemStack)

        event.cancel()
    }
}