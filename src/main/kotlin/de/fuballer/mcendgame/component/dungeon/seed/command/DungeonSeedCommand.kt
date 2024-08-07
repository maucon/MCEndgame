package de.fuballer.mcendgame.component.dungeon.seed.command

import de.fuballer.mcendgame.component.dungeon.seed.DungeonSeedSettings
import de.fuballer.mcendgame.component.dungeon.seed.db.DungeonSeedEntity
import de.fuballer.mcendgame.component.dungeon.seed.db.DungeonSeedRepository
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.ChatUtil
import de.fuballer.mcendgame.util.command.CommandAction
import de.fuballer.mcendgame.util.command.CommandHandler
import de.fuballer.mcendgame.util.command.CommandHelper
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Service
class DungeonSeedCommand(
    private val playerDungeonSeedRepo: DungeonSeedRepository,
    private val commandHelper: CommandHelper
) : CommandHandler(DungeonSeedSettings.COMMAND_NAME) {
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
            CommandAction.GET -> printPlayerDungeonSeed(args, commandExecutor)
            CommandAction.SET -> setPlayerDungeonSeed(args, commandExecutor)
        }
    }

    private fun printPlayerDungeonSeed(
        args: Array<out String>,
        commandExecutor: Player
    ): Boolean {
        if (args.size > 2) return false

        val targetPlayer =
            if (args.size == 1) commandExecutor
            else {
                commandHelper.getPlayer(commandExecutor, args[1]) ?: return true
            }

        val entity = playerDungeonSeedRepo.findById(targetPlayer.uniqueId)
        if (entity == null) {
            commandExecutor.sendMessage(DungeonSeedSettings.PLAYER_NO_SEED)
            return true
        }

        ChatUtil.sendCopyableText(
            commandExecutor,
            DungeonSeedSettings.getPrefix(commandExecutor),
            entity.seed,
            DungeonSeedSettings.SUFFIX
        )
        return true
    }

    private fun setPlayerDungeonSeed(
        args: Array<out String>,
        commandExecutor: Player
    ): Boolean {
        if (args.size != 3) return false

        val targetPlayer = commandHelper.getPlayer(commandExecutor, args[1]) ?: return true

        val seed = args[2]
        if (seed.length !in 1..64) {
            commandExecutor.sendMessage(DungeonSeedSettings.INVALID_SEED)
            return true
        }

        val entity = DungeonSeedEntity(targetPlayer.uniqueId, args[2])
        playerDungeonSeedRepo.save(entity)

        ChatUtil.sendCopyableText(
            commandExecutor,
            DungeonSeedSettings.getPrefix(commandExecutor),
            seed,
            DungeonSeedSettings.SUFFIX
        )
        return true
    }
}
