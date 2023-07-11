package de.fuballer.mcendgame.component.build_calculation

import de.fuballer.mcendgame.framework.stereotype.CommandTabCompleter
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class BuildCalculationTabCompleter : CommandTabCompleter {
    override fun getCommand() = BuildCalculationSettings.COMMAND_NAME

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>?
    ): List<String>? {
        if (sender !is Player) return null
        return listOf("<damage>")
    }
}
