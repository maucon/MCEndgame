package de.fuballer.mcendgame.main.component.corruption

import de.fuballer.mcendgame.main.component.corruption.CorruptionExtensions.isCorrupted
import de.fuballer.mcendgame.main.messaging.misc.*
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import net.minecraft.world.item.ItemStack

@Injectable
object CorruptionUnmodifiableService {
    @CommandHandler
    fun on(cmd: CraftingResultCommand) {
        val stacks = cmd.input.items()
        if (stacks.none { it.isCorrupted() }) return
        cmd.result = ItemStack.EMPTY
    }

    @CommandHandler
    fun on(cmd: CanAnvilForgeCommand) {
        if (cmd.stack0.isCorrupted() || cmd.stack1.isCorrupted()) {
            cmd.canForge = false
        }
    }

    @CommandHandler
    fun on(cmd: CanEnchantItemCommand) {
        if (cmd.itemStack.isCorrupted()) cmd.canEnchant = false
    }

    @CommandHandler
    fun on(cmd: CanSmithCommand) {
        val stack = cmd.input.base
        if (stack.isCorrupted()) cmd.canSmith = false
    }

    @CommandHandler
    fun on(cmd: GrindstoneOutputCommand) {
        if (cmd.firstInput.isCorrupted() || cmd.secondInput.isCorrupted()) cmd.output = ItemStack.EMPTY
    }
}