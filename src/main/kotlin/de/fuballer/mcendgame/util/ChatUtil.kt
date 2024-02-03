package de.fuballer.mcendgame.util

import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.chat.hover.content.Text
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object ChatUtil {
    fun sendCopyableText(
        player: Player,
        prefix: String,
        copyableText: String,
        suffix: String
    ) {
        val msgPrefix = TextComponent(prefix)
        val msgSuffix = TextComponent(suffix)

        val copyPart = TextComponent("${ChatColor.GREEN}$copyableText")
        copyPart.hoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, Text("Click to Copy"))
        copyPart.clickEvent = ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, copyableText)

        player.spigot().sendMessage(msgPrefix, copyPart, msgSuffix)
    }
}