package de.fuballer.mcendgame.component.dungeon.progress.command

import de.fuballer.mcendgame.component.dungeon.progress.PlayerDungeonProgressSettings
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.framework.stereotype.CommandTabCompleter
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

@Service
class DungeonProgressTabCompleter : CommandTabCompleter {
    override fun initialize(plugin: JavaPlugin) {
        plugin.getCommand(PlayerDungeonProgressSettings.COMMAND_NAME)!!.tabCompleter = this
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String>? {
        if (sender !is Player) return null

        return when (args.size) {
            1 -> listOf("get", "set")
            2 -> PluginUtil.getOfflinePlayers().mapNotNull { it.name }
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
