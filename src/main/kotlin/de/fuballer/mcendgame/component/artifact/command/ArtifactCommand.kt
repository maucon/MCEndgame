package de.fuballer.mcendgame.component.artifact.command

import de.fuballer.mcendgame.component.artifact.ArtifactSettings
import de.fuballer.mcendgame.domain.persistent_data.TypeKeys
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.CommandHandler
import de.fuballer.mcendgame.util.ArtifactUtil
import de.fuballer.mcendgame.util.PersistentDataUtil
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.lang.Integer.min

@Component
class ArtifactCommand : CommandHandler {
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
        val artifacts = PersistentDataUtil.getValue(player, TypeKeys.ARTIFACTS) ?: listOf()

        val itemsStacks = artifacts.map { ArtifactUtil.getItem(it) }
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