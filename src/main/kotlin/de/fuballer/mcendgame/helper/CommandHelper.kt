package de.fuballer.mcendgame.helper

import de.fuballer.mcendgame.framework.stereotype.Service
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.Server
import org.bukkit.entity.Player

val ONLINE_PLAYER_NOT_FOUND_MESSAGE = "${ChatColor.RED}Player not found or online!"
val OFFLINE_PLAYER_NOT_FOUND_MESSAGE = "${ChatColor.RED}Player not found!"

class CommandHelper(
    private val server: Server
) : Service {
    fun getCommandAction(arg: String) = CommandAction.entries.find { it.actionName == arg }

    fun getOnlinePlayer(commandExecutor: Player, playerName: String): Player? {
        val player = server.getPlayer(playerName)
        if (player != null) return player;

        commandExecutor.sendMessage(ONLINE_PLAYER_NOT_FOUND_MESSAGE)
        return null
    }

    fun getPlayer(commandExecutor: Player, playerName: String): OfflinePlayer? {
        val player = PluginUtil.getOfflinePlayers()
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
