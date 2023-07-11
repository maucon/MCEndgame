package de.fuballer.mcendgame.component.artifact.command

import de.fuballer.mcendgame.component.artifact.ArtifactSettings
import de.fuballer.mcendgame.framework.stereotype.CommandTabCompleter
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ArtifactTabCompleter : CommandTabCompleter {
    override fun getCommand() = ArtifactSettings.COMMAND_NAME

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
