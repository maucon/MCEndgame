package de.fuballer.mcendgame.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player

object ChatUtil {
    fun sendCopyableText(
        player: Player,
        prefix: Component,
        copyableText: String,
        suffix: Component
    ) {
        val message = Component.textOfChildren(
            prefix,
            TextComponent.create(copyableText, NamedTextColor.GREEN),
            suffix
        )
            .clickEvent(ClickEvent.copyToClipboard(copyableText))
            .hoverEvent(HoverEvent.showText(TextComponent.create("Click to Copy")))

        player.sendMessage(message)
    }
}