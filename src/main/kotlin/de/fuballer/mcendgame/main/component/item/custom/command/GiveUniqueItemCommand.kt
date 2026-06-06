package de.fuballer.mcendgame.main.component.item.custom.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import de.fuballer.mcendgame.main.component.item.custom.UniqueAttributesItemInterface
import de.fuballer.mcendgame.main.util.extension.ServerCommandSourceExtension.isModerator
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.world.item.Item

@Injectable
class GiveUniqueItemCommand {
    companion object {
        private const val NAME = "giveunique"
        private const val UNIQUE_ITEM_ARGUMENT = "unique-item"
        private const val DOUBLE_ROLLS_ARGUMENT = "rolls"
    }

    @Initializer
    fun register() = CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, _, _ ->
        dispatcher.register(
            Commands.literal(NAME)
                .requires { it.isModerator() }
                .then(
                    Commands.argument(UNIQUE_ITEM_ARGUMENT, UniqueItemArgumentType())
                        .suggests(UniqueItemSuggestionProvider())
                        .executes { giveUniqueItem(it, false) }
                        .then(
                            Commands.argument(DOUBLE_ROLLS_ARGUMENT, StringArgumentType.greedyString())
                                .executes { giveUniqueItem(it, true) }
                        )
                )
        )
    })

    private fun giveUniqueItem(
        context: CommandContext<CommandSourceStack>,
        hasRolls: Boolean
    ): Int {
        val player = context.source.player ?: return 0
        val uniqueItem = context.getArgument(UNIQUE_ITEM_ARGUMENT, Item::class.java)
        if (uniqueItem !is UniqueAttributesItemInterface) return 0

        val rolls = if (!hasRolls) listOf()
        else context.getArgument(DOUBLE_ROLLS_ARGUMENT, String::class.java).split(" ").mapNotNull { it.toDoubleOrNull() }

        player.addItem(uniqueItem.getRolledStack(uniqueItem, rolls))

        return Command.SINGLE_SUCCESS
    }
}