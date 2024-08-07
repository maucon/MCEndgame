package de.fuballer.mcendgame.util.command

import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.PluginUtil
import de.fuballer.mcendgame.util.TextComponent
import org.bukkit.OfflinePlayer
import org.bukkit.Server
import org.bukkit.entity.Player

private val ONLINE_PLAYER_NOT_FOUND_MESSAGE = TextComponent.error("No player was found")
private val OFFLINE_PLAYER_NOT_FOUND_MESSAGE = TextComponent.error("No player was found")

@Service
class CommandHelper(
    private val server: Server
) {
    fun getCommandAction(arg: String) = CommandAction.entries.find { it.actionName == arg }

    fun getOnlinePlayer(commandExecutor: Player, playerName: String): Player? {
        val player = server.getPlayer(playerName)
        if (player != null) return player

        commandExecutor.sendMessage(ONLINE_PLAYER_NOT_FOUND_MESSAGE)
        return null
    }

    fun getPlayer(commandExecutor: Player, playerName: String): OfflinePlayer? {
        val player =
            PluginUtil.getOnlinePlayers().find { it.name == playerName }
                ?: PluginUtil.getOfflinePlayers().find { it.name == playerName }

        if (player != null) return player

        commandExecutor.sendMessage(OFFLINE_PLAYER_NOT_FOUND_MESSAGE)
        return null
    }
}