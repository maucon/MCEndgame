package de.fuballer.mcendgame.component.dungeon.type.command

import de.fuballer.mcendgame.component.dungeon.type.DungeonType
import de.fuballer.mcendgame.component.dungeon.type.DungeonTypeSettings
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.framework.stereotype.CommandTabCompleter
import de.fuballer.mcendgame.util.PluginUtil
import de.fuballer.mcendgame.util.command.CommandAction
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

@Service
class DungeonTypeTabCompleter : CommandTabCompleter {
    override fun initialize(plugin: JavaPlugin) {
        plugin.getCommand(DungeonTypeSettings.COMMAND_NAME)!!.tabCompleter = this
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): List<String>? {
        if (sender !is Player) return null

        return when (args.size) {
            1 -> CommandAction.allNames()
            2 -> PluginUtil.getOfflinePlayers().mapNotNull { it.name }
            3 -> {
                if (args[0] == CommandAction.GET.actionName) return listOf()
                return DungeonType.entries
                    .map { it.name }
                    .filter { it.contains(args[2], true) }
            }

            else -> listOf()
        }
    }
}
