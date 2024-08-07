package de.fuballer.mcendgame.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration

object TextComponent {
    fun create(text: String, color: TextColor = NamedTextColor.WHITE) =
        Component.text(text).color(color).decoration(TextDecoration.ITALIC, false)

    fun empty() = Component.empty()

    fun error(text: String) =
        create(text, NamedTextColor.RED)
}