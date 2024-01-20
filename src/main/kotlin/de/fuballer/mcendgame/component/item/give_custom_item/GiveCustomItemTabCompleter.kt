package de.fuballer.mcendgame.component.item.give_custom_item

import de.fuballer.mcendgame.domain.item.CustomItemType
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.CommandTabCompleter
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

@Component
class GiveCustomItemTabCompleter : CommandTabCompleter {
    override fun initialize(plugin: JavaPlugin) {
        plugin.getCommand(GiveCustomItemSettings.COMMAND_NAME)!!.tabCompleter = this
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String>? {
        if (sender !is Player) return null

        return when (args.size) {
            1 -> PluginUtil.getOnlinePlayers().map { it.name }
            2 -> CustomItemType.REGISTRY.keys
                .filter { it.contains(args[1], true) }

            3 -> (0..100)
                .map { it.toString() }
                .filter { it.contains(args[2], true) }

            else -> listOf()
        }
    }
}
