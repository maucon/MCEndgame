package de.fuballer.mcendgame.component.killer.command

import de.fuballer.mcendgame.component.killer.KillerSettings
import de.fuballer.mcendgame.component.killer.db.KillerRepository
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.command.CommandHandler
import de.fuballer.mcendgame.util.command.CommandHelper
import org.bukkit.OfflinePlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Service
class KillerCommand(
    private val killerRepo: KillerRepository,
    private val commandHelper: CommandHelper
) : CommandHandler(KillerSettings.COMMAND_NAME) {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        val commandExecutor = sender as? Player ?: return false

        if (args.isEmpty()) {
            showKiller(commandExecutor, commandExecutor)
            return true
        }

        val targetPlayer = commandHelper.getPlayer(commandExecutor, args[0]) ?: return true
        showKiller(commandExecutor, targetPlayer)
        return true
    }

    private fun showKiller(
        commandExecutor: Player,
        targetPlayer: OfflinePlayer
    ) {
        val killer = killerRepo.findById(targetPlayer.uniqueId)
        if (killer == null) {
            commandExecutor.sendMessage(KillerSettings.NO_KILLER_FOUND_MESSAGE)
            return
        }

        val inventory = killer.createInventory(targetPlayer.name!!)
        commandExecutor.openInventory(inventory)
    }
}