package de.fuballer.mcendgame.component.totem.data

import org.bukkit.ChatColor

enum class TotemTier(
    val tier: Int,
    val color: ChatColor
) {
    COMMON(1, ChatColor.WHITE),
    UNCOMMON(2, ChatColor.GREEN),
    RARE(3, ChatColor.BLUE),
    LEGENDARY(4, ChatColor.GOLD)
}