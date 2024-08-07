package de.fuballer.mcendgame.component.statistics.command

import de.fuballer.mcendgame.component.statistics.StatisticsSettings
import de.fuballer.mcendgame.component.statistics.db.StatisticsRepository
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.command.CommandHandler
import de.fuballer.mcendgame.util.command.CommandHelper
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*

@Service
class StatisticsCommand(
    private val statisticsRepo: StatisticsRepository,
    private val commandHelper: CommandHelper
) : CommandHandler(StatisticsSettings.COMMAND_NAME) {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        val commandExecutor = sender as? Player ?: return true

        if (args.isEmpty()) {
            showStatistics(commandExecutor, commandExecutor.uniqueId)
            return true
        }

        val targetPlayer = commandHelper.getPlayer(commandExecutor, args[0]) ?: return true
        showStatistics(commandExecutor, targetPlayer.uniqueId)

        return true
    }

    private fun showStatistics(
        commandExecutor: Player,
        targetPlayer: UUID
    ) {
        val statistics = statisticsRepo.findById(targetPlayer)
        if (statistics == null) {
            commandExecutor.sendMessage(StatisticsSettings.PLAYER_NO_STATISTICS_MESSAGE)
            return
        }

        val book = statistics.createBook()
        commandExecutor.openBook(book)
    }
}