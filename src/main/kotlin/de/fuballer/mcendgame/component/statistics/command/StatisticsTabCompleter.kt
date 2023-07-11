package de.fuballer.mcendgame.component.statistics.command

import de.fuballer.mcendgame.component.statistics.StatisticsSettings
import de.fuballer.mcendgame.framework.stereotype.CommandTabCompleter
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class StatisticsTabCompleter : CommandTabCompleter {
    override fun getCommand() = StatisticsSettings.COMMAND_NAME

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String>? {
        if (sender !is Player) return null
        return when (args.size) {
            1 -> Bukkit.getOfflinePlayers().mapNotNull { it.name }
            else -> listOf()
        }
    }
}
