package de.fuballer.mcendgame.component.killer.command

import de.fuballer.mcendgame.component.killer.KillerSettings
import de.fuballer.mcendgame.framework.stereotype.CommandTabCompleter
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class KillerTabCompleter : CommandTabCompleter {
    override fun getCommand() = KillerSettings.COMMAND_NAME

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
