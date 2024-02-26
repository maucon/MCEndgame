package de.fuballer.mcendgame.component.totem.command

import de.fuballer.mcendgame.component.inventory.CustomInventoryType
import de.fuballer.mcendgame.component.totem.TotemSettings
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.CommandHandler
import de.fuballer.mcendgame.util.InventoryUtil
import de.fuballer.mcendgame.util.extension.PlayerExtension.getTotems
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.lang.Integer.min

@Component
class TotemCommand : CommandHandler(TotemSettings.COMMAND_NAME) {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        val commandExecutor = sender as? Player ?: return false
        openTotemsWindow(commandExecutor)
        return true
    }

    private fun openTotemsWindow(player: Player) {
        val totems = player.getTotems() ?: listOf()

        val itemsStacks = totems.map { it.toItem() }
        showTotemsWindow(player, itemsStacks)
    }

    private fun showTotemsWindow(player: Player, totems: List<ItemStack>) {
        val inventory = InventoryUtil.createInventory(
            TotemSettings.TOTEM_WINDOW_TYPE,
            TotemSettings.TOTEM_WINDOW_TITLE,
            CustomInventoryType.TOTEM
        )

        for (i in 0 until min(totems.size, TotemSettings.TOTEM_WINDOW_SIZE)) {
            inventory.setItem(i, totems[i])
        }

        player.openInventory(inventory)
    }
}