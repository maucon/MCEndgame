package de.fuballer.mcendgame.component.artifact.command.give_artifact

import de.fuballer.mcendgame.component.artifact.ArtifactSettings
import de.fuballer.mcendgame.domain.ArtifactType
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.CommandTabCompleter
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

@Component
class GiveArtifactTabCompleter : CommandTabCompleter {
    override fun initialize(plugin: JavaPlugin) {
        plugin.getCommand(ArtifactSettings.GIVE_ARTIFACT_COMMAND_NAME)!!.tabCompleter = this
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
            2 -> ArtifactType.entries
                .map(Enum<*>::name)
                .filter { it.contains(args[1], true) }

            3 -> listOf("0", "1", "2", "3")
                .filter { it.contains(args[2], true) }

            else -> listOf()
        }
    }
}
