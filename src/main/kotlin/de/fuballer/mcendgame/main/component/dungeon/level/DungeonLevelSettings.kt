package de.fuballer.mcendgame.main.component.dungeon.level

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent

object DungeonLevelSettings {
    const val LEVEL_INCREASE_THRESHOLD = 3
    const val CLIENT_SET_LEVEL_LIMIT_PERCENTAGE = 0.75 // percentage of highest reached

    fun getClientSetLevelLimit(highestReached: Int) = (highestReached * CLIENT_SET_LEVEL_LIMIT_PERCENTAGE).toInt()

    private val DUNGEON_COMPLETED_PREFIX: MutableComponent = Component.translatable("text.mcendgame.dungeon_level.dungeon_completed")
        .withStyle(ChatFormatting.GOLD, ChatFormatting.BOLD)

    val COMPLETION_LOCKED_MESSAGE: Component = DUNGEON_COMPLETED_PREFIX.copy().append(
        Component.translatable("text.mcendgame.dungeon_level.locked")
            .withStyle { style -> style.withBold(false).withColor(ChatFormatting.RED) }
    )

    val REGRESS_LOCKED_MESSAGE: Component = Component.translatable("text.mcendgame.dungeon_level.dungeon_death").withStyle(ChatFormatting.RED, ChatFormatting.BOLD).append(
        Component.translatable("text.mcendgame.dungeon_level.locked")
            .withStyle { style -> style.withBold(false).withColor(ChatFormatting.GRAY) }
    )

    val NO_PROGRESS_MESSAGE: Component = DUNGEON_COMPLETED_PREFIX.copy().append(
        Component.translatable("text.mcendgame.dungeon_level.no_progress")
            .withStyle { style -> style.withBold(false).withColor(ChatFormatting.RED) }
    )

    fun getRegressMessage(level: Int, progress: Int): Component =
        Component.translatable("text.mcendgame.dungeon_level.dungeon_death").withStyle(ChatFormatting.RED, ChatFormatting.BOLD)
            .append(
                Component.translatable("text.mcendgame.dungeon_level.regress", level, progress, LEVEL_INCREASE_THRESHOLD)
                    .withStyle { style -> style.withBold(false).withColor(ChatFormatting.AQUA) }
            )

    fun getProgressMessage(level: Int, progress: Int): Component = DUNGEON_COMPLETED_PREFIX.copy().append(
        Component.translatable("text.mcendgame.dungeon_level.progress", level, progress, LEVEL_INCREASE_THRESHOLD)
            .withStyle { style -> style.withBold(false).withColor(ChatFormatting.AQUA) }
    )
}