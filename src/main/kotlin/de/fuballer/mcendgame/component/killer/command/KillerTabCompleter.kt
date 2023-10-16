package de.fuballer.mcendgame.component.killer.command

import de.fuballer.mcendgame.component.killer.KillerSettings
import de.fuballer.mcendgame.framework.stereotype.CommandTabCompleter
import de.fuballer.mcendgame.util.PluginUtil
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
            1 -> PluginUtil.getOfflinePlayers().mapNotNull { it.name }
            else -> listOf()
        }
    }
}
