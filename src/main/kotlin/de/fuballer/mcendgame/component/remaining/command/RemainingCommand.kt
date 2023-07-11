package de.fuballer.mcendgame.component.remaining.command

import de.fuballer.mcendgame.component.remaining.RemainingSettings
import de.fuballer.mcendgame.component.remaining.db.RemainingRepository
import de.fuballer.mcendgame.framework.stereotype.CommandHandler
import de.fuballer.mcendgame.helper.WorldHelper
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class RemainingCommand(
    private val remainingRepository: RemainingRepository
) : CommandHandler {
    override fun getCommand() = RemainingSettings.COMMAND_NAME

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        val commandExecutor = sender as? Player ?: return false

        val world = commandExecutor.world
        if (WorldHelper.isNotDungeonWorld(world)) {
            commandExecutor.sendMessage(RemainingSettings.NOT_IN_DUNGEON_ERROR)
            return true
        }

        val entity = remainingRepository.findById(world.name)
        if (entity == null) {
            commandExecutor.sendMessage(RemainingSettings.GENERAL_ERROR)
            return true
        }

        val bossAliveText = if (entity.bossAlive) RemainingSettings.BOSS_ALIVE else RemainingSettings.BOSS_DEAD
        commandExecutor.sendMessage("${RemainingSettings.REMAINING_COLOR}${entity.remaining} ${RemainingSettings.REMAINING_MESSAGE} $bossAliveText")

        return true
    }
}
