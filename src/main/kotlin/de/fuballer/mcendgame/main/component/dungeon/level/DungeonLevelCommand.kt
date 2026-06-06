package de.fuballer.mcendgame.main.component.dungeon.level

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import de.fuballer.mcendgame.main.util.extension.ServerCommandSourceExtension.isModerator
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.getDungeonLevel
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.setDungeonLevel
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import kotlin.math.max

private const val PLAYER_ENTITIES_ARGUMENT = "player_entities"
private const val LEVEL_ARGUMENT = "level"
private const val PROGRESS_ARGUMENT = "progress"
private const val HIGHEST_REACHED_ARGUMENT = "highest_reached"

private const val NO_PLAYER_FOUND_KEY = "commands.mcendgame.set_dungeon_level.no_player"
private const val SET_LEVEL_SINGLE_KEY = "commands.mcendgame.set_dungeon_level.success.single"
private const val SET_LEVEL_MULTIPLE_KEY = "commands.mcendgame.set_dungeon_level.success.multiple"

@Injectable
class DungeonLevelCommand {
    companion object {
        const val NAME = "dungeonlevel"
    }

    @Initializer
    fun register() = CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, _, _ ->
        dispatcher.register(
            Commands.literal(NAME)
                .requires { it.isModerator() }
                .then(
                    Commands.argument(PLAYER_ENTITIES_ARGUMENT, EntityArgument.players())
                        .then(
                            Commands.argument(LEVEL_ARGUMENT, IntegerArgumentType.integer(1))
                                .executes { context ->
                                    val players = EntityArgument.getPlayers(context, PLAYER_ENTITIES_ARGUMENT).toList()
                                    val level = IntegerArgumentType.getInteger(context, LEVEL_ARGUMENT)
                                    execute(context, players, level)
                                }
                                .then(
                                    Commands.argument(PROGRESS_ARGUMENT, IntegerArgumentType.integer(0, DungeonLevelSettings.LEVEL_INCREASE_THRESHOLD - 1))
                                        .executes { context ->
                                            val players = EntityArgument.getPlayers(context, PLAYER_ENTITIES_ARGUMENT).toList()
                                            val level = IntegerArgumentType.getInteger(context, LEVEL_ARGUMENT)
                                            val progress = IntegerArgumentType.getInteger(context, PROGRESS_ARGUMENT)
                                            execute(context, players, level, progress)
                                        }
                                        .then(
                                            Commands.argument(HIGHEST_REACHED_ARGUMENT, IntegerArgumentType.integer())
                                                .executes { context ->
                                                    val players = EntityArgument.getPlayers(context, PLAYER_ENTITIES_ARGUMENT).toList()
                                                    val level = IntegerArgumentType.getInteger(context, LEVEL_ARGUMENT)
                                                    val progress = IntegerArgumentType.getInteger(context, PROGRESS_ARGUMENT)
                                                    val highestReached = IntegerArgumentType.getInteger(context, HIGHEST_REACHED_ARGUMENT)
                                                    execute(context, players, level, progress, highestReached)
                                                }
                                        ))))
        )
    })

    private fun execute(
        context: CommandContext<CommandSourceStack>,
        players: List<ServerPlayer>,
        level: Int,
        progress: Int = 0,
        highestReached: Int? = null,
    ): Int {
        val sender = context.source

        if (players.isEmpty()) {
            sender.sendSystemMessage(Component.translatable(NO_PLAYER_FOUND_KEY).withStyle(ChatFormatting.RED))
            return 0
        }

        players.forEach {
            val current = it.getDungeonLevel()
            val highestReached = highestReached ?: current.highestReached
            val dungeonLevel = PlayerDungeonLevel(level, progress, highestReached, current.locked)
            it.setDungeonLevel(dungeonLevel)
        }

        val count = players.size
        val highestReached = if (highestReached == null) "-" else max(highestReached, level).toString()
        val message = if (count == 1) Component.translatable(SET_LEVEL_SINGLE_KEY, players.first().scoreboardName, level, progress, highestReached)
        else Component.translatable(SET_LEVEL_MULTIPLE_KEY, players.size, level, progress, highestReached)
        sender.sendSystemMessage(message)

        return Command.SINGLE_SUCCESS
    }
}