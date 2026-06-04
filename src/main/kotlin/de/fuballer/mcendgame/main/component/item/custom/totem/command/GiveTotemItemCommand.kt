package de.fuballer.mcendgame.main.component.item.custom.totem.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import de.fuballer.mcendgame.main.component.item.custom.totem.TotemItem
import de.fuballer.mcendgame.main.util.extension.ServerCommandSourceExtension.isModerator
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component
import net.minecraft.util.CommonColors
import net.minecraft.world.item.Item

@Injectable
class GiveTotemItemCommand {
    companion object {
        private const val NAME = "givetotem"
        private const val TOTEM_ITEM_ARGUMENT = "totem-item"
        private const val TIER_ARGUMENT = "tier"
    }

    @Initializer
    fun register() = CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, _, _ ->
        dispatcher.register(
            Commands.literal(NAME)
                .requires {  it.isModerator() }
                .then(
                    Commands.argument(TOTEM_ITEM_ARGUMENT, TotemItemArgumentType())
                        .suggests(TotemItemSuggestionProvider())
                        .executes { giveTotemItem(it, false) }
                        .then(
                            Commands.argument(TIER_ARGUMENT, IntegerArgumentType.integer(0))
                                .executes { giveTotemItem(it, true) }
                        )
                )
        )
    })

    private fun giveTotemItem(
        context: CommandContext<CommandSourceStack>,
        hasSpecifiedTier: Boolean,
    ): Int {
        val player = context.source.player ?: return 0
        val totemItem = context.getArgument(TOTEM_ITEM_ARGUMENT, Item::class.java)
        if (totemItem !is TotemItem) return 0

        val tier = if (hasSpecifiedTier) context.getArgument(TIER_ARGUMENT, Int::class.java) else 0
        if (totemItem.maxTier < tier) {
            player.sendSystemMessage(Component.translatable("error.mcendgame.invalid_totem_tier", totemItem.maxTier).withColor(CommonColors.SOFT_RED))
            return 0
        }

        val stack = totemItem.getStack(tier)
        player.addItem(stack)

        return Command.SINGLE_SUCCESS
    }
}