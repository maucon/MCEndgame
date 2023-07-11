package de.fuballer.mcendgame.helper

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

val ONLINE_PLAYER_NOT_FOUND_MESSAGE = "${ChatColor.RED}Player not found or online!"
val OFFLINE_PLAYER_NOT_FOUND_MESSAGE = "${ChatColor.RED}Player not found!"

object CommandHelper {
    fun getCommandAction(arg: String) = CommandAction.values().find { it.actionName == arg }

    fun getOnlinePlayer(commandExecutor: Player, playerName: String): Player? {
        val player = Bukkit.getPlayer(playerName)
        if (player != null) return player;

        commandExecutor.sendMessage(ONLINE_PLAYER_NOT_FOUND_MESSAGE)
        return null
    }

    fun getPlayer(commandExecutor: Player, playerName: String): OfflinePlayer? {
        val player = Bukkit.getOfflinePlayers()
            .find { it.name == playerName }

        if (player != null) return player;

        commandExecutor.sendMessage(OFFLINE_PLAYER_NOT_FOUND_MESSAGE)
        return null
    }
}

enum class CommandAction(
    val actionName: String
) {
    GET("get"),
    SET("set")
}
