package de.fuballer.mcendgame.component.cosmetics.player.command

import de.fuballer.mcendgame.component.cosmetics.player.PlayerCosmeticsSettings
import de.fuballer.mcendgame.component.cosmetics.player.db.PlayerCosmeticsEntity
import de.fuballer.mcendgame.component.cosmetics.player.db.PlayerCosmeticsRepository
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.CommandHandler
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


@Component
class PlayerCosmeticsCommand(
    private val playerCosmeticsRepository: PlayerCosmeticsRepository
) : CommandHandler(PlayerCosmeticsSettings.COMMAND_NAME) {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        val commandExecutor = sender as? Player ?: return false

        showCosmetics(commandExecutor)
        return true
    }

    private fun showCosmetics(
        commandExecutor: Player
    ) {
        var playerCosmeticsEntity = playerCosmeticsRepository.findById(commandExecutor.uniqueId)
        if (playerCosmeticsEntity == null) {
            playerCosmeticsEntity = PlayerCosmeticsEntity(commandExecutor.uniqueId)
            playerCosmeticsRepository.save(playerCosmeticsEntity)
        }

        val inventory = playerCosmeticsEntity.createInventory()
        commandExecutor.openInventory(inventory)
    }
}
