package de.fuballer.mcendgame.component.cosmetics.player

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack

object PlayerCosmeticsSettings {
    const val COMMAND_NAME = "cosmetics"

    val COSMETIC_ITEM_LORE_TAG = "${ChatColor.LIGHT_PURPLE}COSMETIC"

    const val INVENTORY_TITLE = "Cosmetics"
    val INVENTORY_TYPE = InventoryType.HOPPER

    const val SHOW_HELMET_INVENTORY_INDEX = 0

    val EQUIPMENT_INVENTORY_INDICES = mapOf(
        EquipmentSlot.HEAD to 1,
        EquipmentSlot.CHEST to 2,
        EquipmentSlot.LEGS to 3,
        EquipmentSlot.FEET to 4,
    )

    val SHOW_HELMET_ITEM = ItemStack(Material.LIME_STAINED_GLASS_PANE).apply {
        val itemMeta = this.itemMeta ?: return@apply
        itemMeta.setDisplayName(ChatColor.GREEN.toString() + "Show Helmet")
        this.setItemMeta(itemMeta)
    }
    val HIDE_HELMET_ITEM = ItemStack(Material.RED_STAINED_GLASS_PANE).apply {
        val itemMeta = this.itemMeta ?: return@apply
        itemMeta.setDisplayName(ChatColor.RED.toString() + "Hide Helmet")
        this.setItemMeta(itemMeta)
    }
}