package de.fuballer.mcendgame.main.component.killer

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.GameProfileArgument
import net.minecraft.network.chat.Component
import net.minecraft.server.players.NameAndId

private const val PLAYER_CONFIG_ARGUMENT = "player_config"
private const val NO_KILLER_KEY = "commands.mcendgame.killer.no_killer"

@Injectable
class KillerCommand(
    private val killerService: KillerService,
) {
    companion object {
        const val NAME = "killer"
    }

    @Initializer
    fun register() = CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, _, _ ->
        dispatcher.register(
            Commands.literal(NAME)
                .executes { context -> execute(context) }
                .then(
                    Commands.argument(PLAYER_CONFIG_ARGUMENT, GameProfileArgument.gameProfile())
                        .executes { context ->
                            val configs = GameProfileArgument.getGameProfiles(context, PLAYER_CONFIG_ARGUMENT)
                            val config = if (configs.isNotEmpty()) configs.first() else null
                            execute(context, config)
                        }
                )
        )
    })

    fun execute(
        context: CommandContext<CommandSourceStack>,
        killedConfigEntry: NameAndId? = null,
    ): Int {
        val player = context.source.player ?: return 0
        val killedUUID = killedConfigEntry?.id ?: player.uuid

        if (!killerService.openKillerInventory(player, killedUUID)) {
            val name = killedConfigEntry?.name ?: player.name
            player.sendSystemMessage(Component.translatable(NO_KILLER_KEY, name).withStyle(ChatFormatting.RED))
            return 0
        }

        return Command.SINGLE_SUCCESS
    }
}