package de.fuballer.mcendgame.component.statistics

import de.fuballer.mcendgame.configuration.PluginConfiguration
import de.fuballer.mcendgame.util.TextComponent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

object StatisticsSettings {
    const val COMMAND_NAME = "dungeon-statistics"
    val PLAYER_NO_STATISTICS_MESSAGE = TextComponent.error("Player does not have dungeon statistics")

    val STATISTICS_BOOK_AUTHOR = PluginConfiguration.plugin().name
    const val STATISTICS_BOOK_TITLE = "ItemStats"

    private val STATISTICS_BOOK_HEADLINE_COLOR = NamedTextColor.BLACK
    private val STATISTICS_BOOK_HEADLINE_DECORATION = TextDecoration.BOLD

    val STATISTICS_BOOK_TEXT_COLOR: NamedTextColor = NamedTextColor.BLACK

    val STATISTICS_BOOK_DUNGEONS_HEADLINE = TextComponent.create("Dungeons", STATISTICS_BOOK_HEADLINE_COLOR).decorate(STATISTICS_BOOK_HEADLINE_DECORATION)
    val STATISTICS_BOOK_COMBAT_HEADLINE = TextComponent.create("Combat", STATISTICS_BOOK_HEADLINE_COLOR).decorate(STATISTICS_BOOK_HEADLINE_DECORATION)

    val DUNGEONS_OPENED_TEXT = TextComponent.create("Opened:", NamedTextColor.DARK_BLUE)
    val DUNGEONS_COMPLETED_TEXT = TextComponent.create("Completed:", NamedTextColor.DARK_GREEN)
    val HIGHEST_DUNGEONS_COMPLETED_TEXT = TextComponent.create("Highest Tier:", NamedTextColor.GOLD)

    val TOTAL_KILLS_TEXT = TextComponent.create("Kills:", NamedTextColor.DARK_GREEN)
    val BOSS_KILLS_TEXT = TextComponent.create("Boss Kills:", NamedTextColor.DARK_BLUE)
    val LOOT_GOBLINS_KILLS_TEXT = TextComponent.create("Loot Goblin Kills:", NamedTextColor.DARK_PURPLE)
    val ELITE_KILLS_TEXT = TextComponent.create("Elite Kills:", NamedTextColor.GOLD)
    val DEATHS_TEXT = TextComponent.create("Deaths:", NamedTextColor.DARK_RED)
}