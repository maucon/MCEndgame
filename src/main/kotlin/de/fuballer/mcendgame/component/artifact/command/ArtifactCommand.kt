package de.fuballer.mcendgame.component.artifact.command

import de.fuballer.mcendgame.component.artifact.ArtifactSettings
import de.fuballer.mcendgame.component.inventory.CustomInventoryType
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.CommandHandler
import de.fuballer.mcendgame.util.InventoryUtil
import de.fuballer.mcendgame.util.extension.PlayerExtension.getArtifacts
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.lang.Integer.min

@Component
class ArtifactCommand : CommandHandler(ArtifactSettings.COMMAND_NAME) {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        val commandExecutor = sender as? Player ?: return false
        openArtifactsWindow(commandExecutor)
        return true
    }

    private fun openArtifactsWindow(player: Player) {
        val artifacts = player.getArtifacts() ?: listOf()

        val itemsStacks = artifacts.map { it.toItem() }
        showArtifactsWindow(player, itemsStacks)
    }

    private fun showArtifactsWindow(player: Player, artifacts: List<ItemStack>) {
        val inventory = InventoryUtil.createInventory(
            ArtifactSettings.ARTIFACTS_WINDOW_TYPE,
            ArtifactSettings.ARTIFACTS_WINDOW_TITLE,
            CustomInventoryType.ARTIFACT
        )

        for (i in 0 until min(artifacts.size, ArtifactSettings.ARTIFACTS_WINDOW_SIZE)) {
            inventory.setItem(i, artifacts[i])
        }

        player.openInventory(inventory)
    }
}