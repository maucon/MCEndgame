package de.fuballer.mcendgame.main.component.totem

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import de.maucon.mauconframework.di.annotation.Injectable
import de.maucon.mauconframework.initializer.Initializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

@Injectable
class TotemCommand(
    private val totemService: TotemService,
) {
    companion object {
        const val NAME = "totems"
    }

    @Initializer
    fun register() = CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher, _, _ ->
        dispatcher.register(
            Commands.literal(NAME)
                .executes { context -> execute(context) }
        )
    })

    fun execute(
        context: CommandContext<CommandSourceStack>,
    ): Int {
        val player = context.source.player ?: return 0
        totemService.openInventory(player)
        return Command.SINGLE_SUCCESS
    }
}