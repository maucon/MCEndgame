package de.fuballer.mcendgame.component.artifact.command.give_artifact

import de.fuballer.mcendgame.component.artifact.ArtifactSettings
import de.fuballer.mcendgame.component.artifact.data.Artifact
import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.helper.CommandHelper
import de.fuballer.mcendgame.technical.CommandHandler
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
        if (args.size < 3) return false

        val targetPlayer = commandHelper.getOnlinePlayer(sender, args[0]) ?: return true
        val type = ArtifactType.REGISTRY[args[1]] ?: return false

        if (!ArtifactTier.entries.map { it.name }.contains(args[2].uppercase())) return false
        val tier = ArtifactTier.valueOf(args[2].uppercase())

        val artifact = Artifact(type, tier)
        val artifactItem = artifact.toItem()
        targetPlayer.inventory.addItem(artifactItem)

        return true
    }
}