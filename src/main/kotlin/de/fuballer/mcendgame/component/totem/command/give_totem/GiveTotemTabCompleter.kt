package de.fuballer.mcendgame.component.totem.command.give_totem

import de.fuballer.mcendgame.component.totem.TotemSettings
import de.fuballer.mcendgame.component.totem.data.TotemTier
import de.fuballer.mcendgame.component.totem.data.TotemType
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.CommandTabCompleter
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

@Component
class GiveTotemTabCompleter : CommandTabCompleter {
    override fun initialize(plugin: JavaPlugin) {
        plugin.getCommand(TotemSettings.GIVE_COMMAND_NAME)!!.tabCompleter = this
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
            2 -> TotemType.REGISTRY.keys
                .filter { it.contains(args[1], true) }

            3 -> TotemTier.entries
                .map { it.name }
                .filter { it.contains(args[2], true) }

            else -> listOf()
        }
    }
}
