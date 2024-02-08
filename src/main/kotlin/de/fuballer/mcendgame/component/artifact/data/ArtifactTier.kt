package de.fuballer.mcendgame.component.artifact.data

import org.bukkit.ChatColor

enum class ArtifactTier(
    val color: ChatColor
) {
    COMMON(ChatColor.WHITE),
    UNCOMMON(ChatColor.GREEN),
    RARE(ChatColor.BLUE),
    LEGENDARY(ChatColor.GOLD)
}