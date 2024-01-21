package de.fuballer.mcendgame.component.item

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.ItemUtil
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.enchantment.EnchantItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.inventory.PrepareAnvilEvent
import org.bukkit.event.inventory.PrepareSmithingEvent

@Component
class GeneratedItemService : Listener {
    @EventHandler
    fun on(event: EnchantItemEvent) {
        val item = event.item
        val enchants = event.enchantsToAdd
        val damageAllTier = enchants[Enchantment.DAMAGE_ALL] ?: return

        item.addEnchantment(Enchantment.DAMAGE_ALL, damageAllTier)
        ItemUtil.updateAttributesAndLore(item)
    }

    @EventHandler
    fun on(event: PrepareAnvilEvent) {
        val item = event.result ?: return
        if (ItemUtil.isVanillaItem(item)) return

        ItemUtil.updateAttributesAndLore(item)
    }

    @EventHandler
    fun on(event: PrepareSmithingEvent) {
        val item = event.result ?: return
        if (ItemUtil.isVanillaItem(item)) return

        ItemUtil.updateAttributesAndLore(item)
    }

    @EventHandler
    fun on(event: InventoryClickEvent) {
        val inventory = event.inventory
        if (inventory.type != InventoryType.GRINDSTONE || event.rawSlot != 2) return

        val item = inventory.getItem(2) ?: return
        if (ItemUtil.isVanillaItem(item)) return

        ItemUtil.updateAttributesAndLore(item)
    }
}