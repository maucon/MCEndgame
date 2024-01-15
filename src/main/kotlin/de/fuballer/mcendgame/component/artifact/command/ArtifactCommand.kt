package de.fuballer.mcendgame.component.artifact.command

import de.fuballer.mcendgame.component.artifact.ArtifactService
import de.fuballer.mcendgame.component.artifact.ArtifactSettings
import de.fuballer.mcendgame.component.artifact.db.ArtifactEntity
import de.fuballer.mcendgame.component.artifact.db.ArtifactRepository
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.CommandHandler
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.lang.Integer.min

@Component
class ArtifactCommand(
    private val artifactRepo: ArtifactRepository,
    private val artifactService: ArtifactService
) : CommandHandler {
    override fun initialize(plugin: JavaPlugin) = plugin.getCommand(ArtifactSettings.COMMAND_NAME)!!.setExecutor(this)

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
        val uuid = player.uniqueId
        val entity = artifactRepo.findById(uuid)
            ?: artifactRepo.save(ArtifactEntity(uuid))

        val itemsStacks = entity.artifacts.map { artifactService.getArtifactAsItem(it) }
        showArtifactsWindow(player, itemsStacks)
    }

    private fun showArtifactsWindow(player: Player, artifacts: List<ItemStack>) {
        val inventory = PluginUtil.createInventory(
            ArtifactSettings.ARTIFACTS_WINDOW_TYPE,
            ArtifactSettings.ARTIFACTS_WINDOW_TITLE
        )

        for (i in 0 until min(artifacts.size, ArtifactSettings.ARTIFACTS_WINDOW_SIZE)) {
            inventory.setItem(i, artifacts[i])
        }

        player.openInventory(inventory)
    }
}
