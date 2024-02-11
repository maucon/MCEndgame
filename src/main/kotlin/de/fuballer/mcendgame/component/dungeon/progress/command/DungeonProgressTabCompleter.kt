package de.fuballer.mcendgame.component.dungeon.progress.command

import de.fuballer.mcendgame.component.dungeon.progress.PlayerDungeonProgressSettings
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.CommandTabCompleter
import de.fuballer.mcendgame.helper.CommandAction
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

@Component
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
            1 -> CommandAction.allNames()
            2 -> PluginUtil.getOfflinePlayers().mapNotNull { it.name }
            3 -> {
                if (args[0] == CommandAction.GET.actionName) return listOf()
                return (1..PlayerDungeonProgressSettings.MAX_DUNGEON_TIER)
                    .map { "$it" }
                    .filter { it.contains(args[2], true) }
            }

            4 -> {
                if (args[0] == CommandAction.GET.actionName) return listOf()
                return (0 until PlayerDungeonProgressSettings.DUNGEON_LEVEL_INCREASE_THRESHOLD)
                    .map { "$it" }
                    .filter { it.contains(args[2], true) }
            }

            else -> listOf()
        }
    }
}