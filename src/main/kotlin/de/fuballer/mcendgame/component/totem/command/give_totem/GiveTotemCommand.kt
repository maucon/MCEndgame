package de.fuballer.mcendgame.component.totem.command.give_totem

import de.fuballer.mcendgame.component.totem.TotemSettings
import de.fuballer.mcendgame.component.totem.data.Totem
import de.fuballer.mcendgame.component.totem.data.TotemTier
import de.fuballer.mcendgame.component.totem.data.TotemType
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.command.CommandHandler
import de.fuballer.mcendgame.util.command.CommandHelper
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Component
class GiveTotemCommand(
    private val commandHelper: CommandHelper
) : CommandHandler(TotemSettings.GIVE_COMMAND_NAME) {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (sender !is Player) return false
        if (args.size < 3) return false

        val targetPlayer = commandHelper.getOnlinePlayer(sender, args[0]) ?: return true
        val type = TotemType.REGISTRY[args[1]] ?: return false

        if (!TotemTier.entries.map { it.name }.contains(args[2].uppercase())) return false
        val tier = TotemTier.valueOf(args[2].uppercase())

        val totem = Totem(type, tier)
        val totemItem = totem.toItem()
        targetPlayer.inventory.addItem(totemItem)

        return true
    }
}