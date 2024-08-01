package de.fuballer.mcendgame.component.dungeon.type.command

import de.fuballer.mcendgame.component.dungeon.type.DungeonType
import de.fuballer.mcendgame.component.dungeon.type.DungeonTypeSettings
import de.fuballer.mcendgame.component.dungeon.type.db.PlayerDungeonTypeEntity
import de.fuballer.mcendgame.component.dungeon.type.db.PlayerDungeonTypeRepository
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.CommandHandler
import de.fuballer.mcendgame.util.ChatUtil
import de.fuballer.mcendgame.util.helper.CommandAction
import de.fuballer.mcendgame.util.helper.CommandHelper
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Component
class DungeonTypeCommand(
    private val playerDungeonTypeRepo: PlayerDungeonTypeRepository,
    private val commandHelper: CommandHelper
) : CommandHandler(DungeonTypeSettings.COMMAND_NAME) {
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
            CommandAction.GET -> printDungeonType(args, commandExecutor)
            CommandAction.SET -> setDungeonType(args, commandExecutor)
        }
    }

    private fun printDungeonType(
        args: Array<out String>,
        commandExecutor: Player
    ): Boolean {
        if (args.size > 2) return false

        val targetPlayer =
            if (args.size == 1) commandExecutor
            else {
                commandHelper.getPlayer(commandExecutor, args[1]) ?: return true
            }

        val entity = playerDungeonTypeRepo.findById(targetPlayer.uniqueId)
        if (entity == null) {
            commandExecutor.sendMessage(DungeonTypeSettings.PLAYER_NO_DUNGEON_TYPE)
            return true
        }

        ChatUtil.sendCopyableText(
            commandExecutor,
            DungeonTypeSettings.getPrefix(commandExecutor),
            entity.dungeonType.toString(),
            DungeonTypeSettings.SUFFIX
        )
        return true
    }

    private fun setDungeonType(
        args: Array<out String>,
        commandExecutor: Player
    ): Boolean {
        if (args.size != 3) return false

        val targetPlayer = commandHelper.getPlayer(commandExecutor, args[1]) ?: return false

        if (!DungeonType.entries.map { it.name }.contains(args[2].uppercase())) return false
        val dungeonType = DungeonType.valueOf(args[2])

        val entity = PlayerDungeonTypeEntity(targetPlayer.uniqueId, dungeonType)
        playerDungeonTypeRepo.save(entity)

        ChatUtil.sendCopyableText(
            commandExecutor,
            DungeonTypeSettings.getPrefix(commandExecutor),
            dungeonType.toString(),
            DungeonTypeSettings.SUFFIX
        )
        return true
    }
}
