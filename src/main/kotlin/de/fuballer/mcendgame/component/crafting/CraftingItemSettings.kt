package de.fuballer.mcendgame.component.crafting

import org.bukkit.ChatColor
import org.bukkit.Material

object CraftingItemSettings {
    val BASE_ITEM = Material.PAPER
    val CRAFTING_ITEM_USAGE_DISCLAIMER = arrayOf(
        "",
        "${ChatColor.GRAY}${ChatColor.ITALIC}Can be used in combination with",
        "${ChatColor.GRAY}${ChatColor.ITALIC}compatible items using an anvil.",
    )
}