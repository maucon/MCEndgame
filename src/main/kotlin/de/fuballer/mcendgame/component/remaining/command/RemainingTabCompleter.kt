package de.fuballer.mcendgame.component.remaining.command

import de.fuballer.mcendgame.component.remaining.RemainingSettings
import de.fuballer.mcendgame.framework.stereotype.CommandTabCompleter
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class RemainingTabCompleter : CommandTabCompleter {
    override fun getCommand() = RemainingSettings.COMMAND_NAME

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
