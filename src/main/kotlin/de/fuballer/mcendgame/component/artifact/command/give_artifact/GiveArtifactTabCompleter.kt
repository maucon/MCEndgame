package de.fuballer.mcendgame.component.artifact.command.give_artifact

import de.fuballer.mcendgame.component.artifact.ArtifactSettings
import de.fuballer.mcendgame.component.artifact.db.ArtifactType
import de.fuballer.mcendgame.framework.stereotype.CommandTabCompleter
import de.fuballer.mcendgame.helper.PluginUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GiveArtifactTabCompleter : CommandTabCompleter {
    override fun getCommand() = ArtifactSettings.GIVE_ARTIFACT_COMMAND_NAME

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): List<String>? {
        if (sender !is Player) return null

        return when (args.size) {
            1 -> PluginUtil.getOnlinePlayers().map { it.name }
            2 -> ArtifactType.values().map(Enum<*>::name).filter { it.startsWith(args[1], true) }
            3 -> listOf("0", "1", "2", "3").filter { it.startsWith(args[2], true) }
            else -> listOf()
        }
    }
}
