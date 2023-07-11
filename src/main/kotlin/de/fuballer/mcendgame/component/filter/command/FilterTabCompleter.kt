package de.fuballer.mcendgame.component.filter.command

import de.fuballer.mcendgame.component.filter.FilterSettings
import de.fuballer.mcendgame.framework.stereotype.CommandTabCompleter
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class FilterTabCompleter : CommandTabCompleter {
    override fun getCommand() = FilterSettings.COMMAND_NAME

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String>? {
        if (sender !is Player) return null
        return listOf()
    }
}
