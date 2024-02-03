package de.fuballer.mcendgame.component.dungeon.seed.command

import de.fuballer.mcendgame.component.dungeon.seed.DungeonSeedSettings
import de.fuballer.mcendgame.domain.technical.persistent_data.TypeKeys
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.CommandHandler
import de.fuballer.mcendgame.helper.CommandHelper
import de.fuballer.mcendgame.util.PersistentDataUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

@Component
class DungeonSeedCommand(
    private val commandHelper: CommandHelper
) : CommandHandler {
    override fun initialize(plugin: JavaPlugin) = plugin.getCommand(DungeonSeedSettings.COMMAND_NAME)!!.setExecutor(this)

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        val commandExecutor = sender as? Player ?: return false
        if (args.size > 1) return false

        val targetPlayer =
            if (args.isEmpty()) commandExecutor
            else {
                commandHelper.getOnlinePlayer(commandExecutor, args[0]) ?: return true
            }

        val seed = PersistentDataUtil.getValue(targetPlayer.world, TypeKeys.SEED)
        if (seed == null) {
            commandExecutor.sendMessage(DungeonSeedSettings.PLAYER_NOT_IN_DUNGEON)
            return true
        }

        val message = DungeonSeedSettings.getSeedMessage(seed)
        commandExecutor.sendMessage(message)

        return true
    }
}
