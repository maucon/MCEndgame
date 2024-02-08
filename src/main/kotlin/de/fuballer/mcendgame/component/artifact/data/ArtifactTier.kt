package de.fuballer.mcendgame.component.artifact.data

import org.bukkit.ChatColor

enum class ArtifactTier(
    val tier: Int,
    val color: ChatColor
) {
    COMMON(1, ChatColor.WHITE),
    UNCOMMON(2, ChatColor.GREEN),
    RARE(3, ChatColor.BLUE),
    LEGENDARY(4, ChatColor.GOLD)
}