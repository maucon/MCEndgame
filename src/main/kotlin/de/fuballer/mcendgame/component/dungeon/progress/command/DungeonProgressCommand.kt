package de.fuballer.mcendgame.component.dungeon.progress.command

import de.fuballer.mcendgame.component.dungeon.progress.PlayerDungeonProgressSettings
import de.fuballer.mcendgame.component.dungeon.progress.db.PlayerDungeonProgressRepository
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.CommandHandler
import de.fuballer.mcendgame.helper.CommandHelper
import de.fuballer.mcendgame.technical.CommandAction
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

@Component
class DungeonProgressCommand(
    private val dungeonProgressRepo: PlayerDungeonProgressRepository,
    private val commandHelper: CommandHelper
) : CommandHandler {
    override fun initialize(plugin: JavaPlugin) = plugin.getCommand(PlayerDungeonProgressSettings.COMMAND_NAME)!!.setExecutor(this)

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        val commandExecutor = sender as? Player ?: return false
        if (args.isEmpty()) return false

        val commandAction = commandHelper.getCommandAction(args[0]) ?: return false
        return when (commandAction) {
            CommandAction.GET -> printDungeonProgress(args, commandExecutor)
            CommandAction.SET -> setDungeonProgress(args, commandExecutor)
        }
    }

    private fun printDungeonProgress(
        args: Array<out String>,
        commandExecutor: Player
    ): Boolean {
        if (args.size > 2) return false

        val targetPlayer =
            if (args.size == 1) commandExecutor
            else {
                commandHelper.getPlayer(commandExecutor, args[1]) ?: return true
            }

        val entity = dungeonProgressRepo.findById(targetPlayer.uniqueId)
        if (entity == null) {
            commandExecutor.sendMessage(PlayerDungeonProgressSettings.PLAYER_NO_PROGRESS_MESSAGE)
            return true
        }

        val message = PlayerDungeonProgressSettings.getDungeonProgressMessage(targetPlayer.name!!, entity.tier, entity.progress)
        commandExecutor.sendMessage(message)

        return true
    }

    private fun setDungeonProgress(
        args: Array<out String>,
        commandExecutor: Player
    ): Boolean {
        if (args.size !in 3..4) return false

        val targetPlayer = commandHelper.getPlayer(commandExecutor, args[1]) ?: return false
        val tier = args[2].toIntOrNull() ?: return false

        val progress =
            if (args.size == 4) args[3].toIntOrNull() ?: return false
            else null

        if (!updateDungeonTier(targetPlayer.uniqueId, tier, progress)) return false

        val entity = dungeonProgressRepo.findById(targetPlayer.uniqueId)
        if (entity == null) {
            commandExecutor.sendMessage(PlayerDungeonProgressSettings.PLAYER_NO_PROGRESS_MESSAGE)
            return true
        }

        val message = PlayerDungeonProgressSettings.getNewDungeonProgressMessage(targetPlayer.name!!, entity.tier, entity.progress)
        commandExecutor.sendMessage(message)

        return true
    }

    private fun updateDungeonTier(
        player: UUID,
        tier: Int,
        progress: Int?
    ): Boolean {
        if (tier !in 1..PlayerDungeonProgressSettings.MAX_DUNGEON_TIER) return false
        progress?.let { if (it !in 0 until PlayerDungeonProgressSettings.DUNGEON_LEVEL_INCREASE_THRESHOLD) return false }

        val entity = dungeonProgressRepo.findById(player) ?: return false

        entity.tier = tier
        progress?.let { entity.progress = it }

        dungeonProgressRepo.save(entity)
        return true
    }
}
