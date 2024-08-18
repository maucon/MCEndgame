package de.fuballer.mcendgame.component.totem.data

import net.kyori.adventure.text.format.NamedTextColor

enum class TotemTier(
    val tier: Int,
    val color: NamedTextColor
) {
    COMMON(1, NamedTextColor.WHITE),
    UNCOMMON(2, NamedTextColor.GREEN),
    RARE(3, NamedTextColor.AQUA),
    LEGENDARY(4, NamedTextColor.GOLD)
}