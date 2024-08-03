package de.fuballer.mcendgame.component.statistics

import de.fuballer.mcendgame.configuration.PluginConfiguration
import org.bukkit.ChatColor

object StatisticsSettings {
    const val COMMAND_NAME = "dungeon-statistics"
    val PLAYER_NO_STATISTICS_MESSAGE = "${ChatColor.RED}Player has no dungeon statistics"

    val STATISTICS_BOOK_AUTHOR = PluginConfiguration.plugin().name
    const val STATISTICS_BOOK_TITLE = "ItemStats"

    private val STATISTICS_BOOK_HEADLINE_COLOR = "" + ChatColor.BLACK + ChatColor.UNDERLINE
    private val STATISTICS_BOOK_TEXT_COLOR = "" + ChatColor.BLACK

    val STATISTICS_BOOK_DUNGEONS_HEADLINE = "${STATISTICS_BOOK_HEADLINE_COLOR}Dungeons"
    val STATISTICS_BOOK_COMBAT_HEADLINE = "${STATISTICS_BOOK_HEADLINE_COLOR}Combat"

    val DUNGEONS_OPENED_TEXT = "${ChatColor.DARK_BLUE}Opened:${STATISTICS_BOOK_TEXT_COLOR}"
    val DUNGEONS_COMPLETED_TEXT = "${ChatColor.DARK_GREEN}Completed:${STATISTICS_BOOK_TEXT_COLOR}"
    val HIGHEST_DUNGEONS_COMPLETED_TEXT = "${ChatColor.GOLD}Highest Tier:${STATISTICS_BOOK_TEXT_COLOR}"

    val TOTAL_KILLS_TEXT = "${ChatColor.DARK_GREEN}Kills:${STATISTICS_BOOK_TEXT_COLOR}"
    val BOSS_KILLS_TEXT = "${ChatColor.DARK_BLUE}Boss Kills:${STATISTICS_BOOK_TEXT_COLOR}"
    val LOOT_GOBLINS_KILLS_TEXT = "${ChatColor.DARK_PURPLE}Loot Goblin Kills:${STATISTICS_BOOK_TEXT_COLOR}"
    val DEATHS_TEXT = "${ChatColor.DARK_RED}Deaths:${STATISTICS_BOOK_TEXT_COLOR}"
    val KILLSTREAK_TEXT = "${ChatColor.GOLD}Highest Killstreak:${STATISTICS_BOOK_TEXT_COLOR}"
}