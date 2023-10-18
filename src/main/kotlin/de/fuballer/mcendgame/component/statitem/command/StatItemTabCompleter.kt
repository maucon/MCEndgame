package de.fuballer.mcendgame.component.statitem.command

import de.fuballer.mcendgame.component.statitem.StatItemSettings
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.framework.stereotype.CommandTabCompleter
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

@Service
class StatItemTabCompleter : CommandTabCompleter {
    override fun initialize(plugin: JavaPlugin) {
        plugin.getCommand(StatItemSettings.COMMAND_NAME)!!.tabCompleter = this
    }

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
