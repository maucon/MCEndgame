package de.fuballer.mcendgame.component.dungeon.seed.command

import de.fuballer.mcendgame.component.dungeon.seed.DungeonSeedSettings
import de.fuballer.mcendgame.domain.technical.CommandAction
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.CommandTabCompleter
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

@Component
class DungeonSeedTabCompleter : CommandTabCompleter {
    override fun initialize(plugin: JavaPlugin) {
        plugin.getCommand(DungeonSeedSettings.COMMAND_NAME)!!.tabCompleter = this
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
            2 -> PluginUtil.getOnlinePlayers().map { it.name }
            3 -> listOf("<seed>")
            else -> listOf()
        }
    }
}