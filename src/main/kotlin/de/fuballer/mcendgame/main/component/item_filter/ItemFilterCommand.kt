package de.fuballer.mcendgame.main.component.item_filter

import com.mojang.brigadier.context.CommandContext
import de.fuballer.mcendgame.main.functional.command.SimpleChatCommand
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.commands.CommandSourceStack

@Injectable
class ItemFilterCommand(
    private val itemFilterService: ItemFilterService,
) : SimpleChatCommand() {
    companion object {
        const val NAME = "dungeonfilter"
    }

    override val name = NAME

    override fun execute(context: CommandContext<CommandSourceStack>) {
        val player = context.source.player ?: return
        itemFilterService.openFilterInventory(player)
    }
}