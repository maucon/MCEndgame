package de.fuballer.mcendgame.component.dungeon.remaining.command

import de.fuballer.mcendgame.component.dungeon.remaining.RemainingSettings
import de.fuballer.mcendgame.component.dungeon.remaining.db.RemainingRepository
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.CommandHandler
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Component
class RemainingCommand(
    private val remainingRepository: RemainingRepository
) : CommandHandler(RemainingSettings.COMMAND_NAME) {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        val commandExecutor = sender as? Player ?: return false

        val world = commandExecutor.world
        if (!world.isDungeonWorld()) {
            commandExecutor.sendMessage(RemainingSettings.NOT_IN_DUNGEON_ERROR)
            return true
        }

        val entity = remainingRepository.findById(world.name)
        if (entity == null) {
            commandExecutor.sendMessage(RemainingSettings.GENERAL_ERROR)
            return true
        }

        val message = RemainingSettings.getRemainingMessage(entity.remaining, entity.bossesSlain, entity.dungeonCompleted)
        commandExecutor.sendMessage(message)

        return true
    }
}
