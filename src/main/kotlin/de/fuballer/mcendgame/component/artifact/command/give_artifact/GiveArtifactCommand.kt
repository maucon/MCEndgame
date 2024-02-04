package de.fuballer.mcendgame.component.artifact.command.give_artifact

import de.fuballer.mcendgame.component.artifact.Artifact
import de.fuballer.mcendgame.component.artifact.ArtifactSettings
import de.fuballer.mcendgame.domain.ArtifactType
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.CommandHandler
import de.fuballer.mcendgame.helper.CommandHelper
import de.fuballer.mcendgame.util.ArtifactUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Component
class GiveArtifactCommand(
    private val commandHelper: CommandHelper
) : CommandHandler(ArtifactSettings.GIVE_ARTIFACT_COMMAND_NAME) {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (sender !is Player) return false
        if (args.size < 3 || !ArtifactType.entries.map { it.name }.contains(args[1].uppercase())) return false

        val targetPlayer = commandHelper.getOnlinePlayer(sender, args[0]) ?: return true

        val type = ArtifactType.valueOf(args[1].uppercase())
        val tier = args[2].toIntOrNull() ?: return false

        val artifact = Artifact(type, tier)
        val artifactItem = ArtifactUtil.getItem(artifact)
        targetPlayer.inventory.addItem(artifactItem)

        return true
    }
}
