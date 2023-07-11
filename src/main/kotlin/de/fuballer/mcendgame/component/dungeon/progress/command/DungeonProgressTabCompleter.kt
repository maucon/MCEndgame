package de.fuballer.mcendgame.component.dungeon.progress.command

import de.fuballer.mcendgame.component.dungeon.progress.PlayerDungeonProgressSettings
import de.fuballer.mcendgame.framework.stereotype.CommandTabCompleter
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class DungeonProgressTabCompleter : CommandTabCompleter {
    override fun getCommand() = PlayerDungeonProgressSettings.COMMAND_NAME

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String>? {
        if (sender !is Player) return null

        return when (args.size) {
            1 -> listOf("get", "set")
            2 -> Bukkit.getOfflinePlayers().mapNotNull { it.name }
            3 -> {
                if (args[0] == "get") return listOf()
                return listOf("<level>")
            }

            4 -> {
                if (args[0] == "get") return listOf()
                return listOf("[<progress>]")
            }

            else -> listOf()
        }
    }
}
