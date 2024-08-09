package de.fuballer.mcendgame.component.item.custom_item.give_custom_item

import de.fuballer.mcendgame.component.item.custom_item.CustomItemType
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.command.CommandHandler
import de.fuballer.mcendgame.util.command.CommandHelper
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Service
class GiveCustomItemCommand(
    private val commandHelper: CommandHelper
) : CommandHandler(GiveCustomItemSettings.COMMAND_NAME) {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        if (sender !is Player) return false
        if (args.size < 2) return false

        val targetPlayer = commandHelper.getOnlinePlayer(sender, args[0]) ?: return true
        val type = CustomItemType.REGISTRY[args[1]] ?: return false

        val rollString = args.getOrNull(2)
        val rollInt = rollString?.toIntOrNull()
            ?.also { if (it !in 0..100) return false }

        val roll = rollInt?.let { it / 100.0 }
        val customItem = CustomItemType.createItem(type, roll)

        targetPlayer.inventory.addItem(customItem)
        return true
    }
}