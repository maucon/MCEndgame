package de.fuballer.mcendgame.main.component.killer

import com.mojang.authlib.GameProfile
import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.argument.GameProfileArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import net.minecraft.util.Formatting

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
            CommandManager.literal(NAME)
                .executes { context -> execute(context) }
                .then(
                    CommandManager.argument(PLAYER_CONFIG_ARGUMENT, GameProfileArgumentType.gameProfile())
                        .executes { context ->
                            val configs = GameProfileArgumentType.getProfileArgument(context, PLAYER_CONFIG_ARGUMENT)
                            val config = if (configs.isNotEmpty()) configs.first() else null
                            execute(context, config)
                        }
                )
        )
    })

    fun execute(
        context: CommandContext<ServerCommandSource>,
        killedConfigEntry: GameProfile? = null,
    ): Int {
        val player = context.source.player ?: return 0
        val killedUUID = killedConfigEntry?.id ?: player.uuid

        if (!killerService.openKillerInventory(player, killedUUID)) {
            val name = killedConfigEntry?.name ?: player.name
            player.sendMessage(Text.translatable(NO_KILLER_KEY, name).formatted(Formatting.RED))
            return 0
        }

        return Command.SINGLE_SUCCESS
    }
}