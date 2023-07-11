package de.fuballer.mcendgame.component.statitem.command

import de.fuballer.mcendgame.component.statitem.StatItemSettings
import de.fuballer.mcendgame.framework.stereotype.CommandTabCompleter
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class StatItemTabCompleter : CommandTabCompleter {
    override fun getCommand() = StatItemSettings.COMMAND_NAME

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
