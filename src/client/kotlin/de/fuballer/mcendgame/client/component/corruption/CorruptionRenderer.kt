package de.fuballer.mcendgame.client.component.corruption

import de.fuballer.mcendgame.client.messaging.RenderItemTooltipCommand
import de.fuballer.mcendgame.main.component.corruption.CorruptionExtensions.isCorrupted
import de.maucon.mauconframework.command.CommandHandler
import de.maucon.mauconframework.di.annotation.Injectable
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component

private val CORRUPTION_TEXT = Component.translatable("mcendgame.corrupted").withStyle(ChatFormatting.DARK_RED)

@Injectable
@Environment(EnvType.CLIENT)
class CorruptionRenderer {
    @CommandHandler
    fun on(cmd: RenderItemTooltipCommand) {
        val stack = cmd.itemStack
        if (!stack.isCorrupted()) return

        val texts = cmd.texts
        if (cmd.tooltipType.isAdvanced) {
            val hasDurabilityLine = stack.damageValue > 0
            val offset = if (hasDurabilityLine) 3 else 2
            texts.add(texts.size - offset, CORRUPTION_TEXT)
        } else {
            texts.add(CORRUPTION_TEXT)
        }
    }
}