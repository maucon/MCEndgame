package de.fuballer.mcendgame.component.statistics.db

import de.fuballer.mcendgame.component.statistics.StatisticsSettings
import de.fuballer.mcendgame.framework.stereotype.Entity
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import java.util.*

data class StatisticsEntity(
    override var id: UUID,

    var dungeonsCompleted: Int = 0,
    var highestCompletedDungeon: Int = 0,
    var dungeonsOpened: Int = 0,
    var totalKills: Int = 0,
    var bossKills: Int = 0,
    var lootGoblinKills: Int = 0,
    var deaths: Int = 0,
    var highestKillstreak: Int = 0,
) : Entity<UUID> {
    fun createBook(): ItemStack {
        val book = ItemStack(Material.WRITTEN_BOOK)
        val bookMeta = book.itemMeta as BookMeta

        bookMeta.author = StatisticsSettings.STATISTICS_BOOK_AUTHOR
        bookMeta.title = StatisticsSettings.STATISTICS_BOOK_TITLE

        val pages = getPages()
        for (page in pages)
            bookMeta.addPage(page)

        book.itemMeta = bookMeta

        return book
    }

    private fun getPages(): List<String> {
        val pages: MutableList<String> = mutableListOf()

        val page1 = createPage(
            listOf(
                StatisticsSettings.STATISTICS_BOOK_DUNGEONS_HEADLINE,
                "",
                StatisticsSettings.DUNGEONS_OPENED_TEXT,
                "   $dungeonsOpened",
                StatisticsSettings.DUNGEONS_COMPLETED_TEXT,
                "   $dungeonsCompleted",
                StatisticsSettings.HIGHEST_DUNGEONS_COMPLETED_TEXT,
                "   $highestCompletedDungeon",
            )
        )
        pages.add(page1)

        val page2 = createPage(
            listOf(
                StatisticsSettings.STATISTICS_BOOK_COMBAT_HEADLINE,
                "",
                StatisticsSettings.TOTAL_KILLS_TEXT,
                "   $totalKills",
                StatisticsSettings.BOSS_KILLS_TEXT,
                "   $bossKills",
                StatisticsSettings.LOOT_GOBLINS_KILLS_TEXT,
                "   $lootGoblinKills",
                StatisticsSettings.DEATHS_TEXT,
                "   $deaths",
                StatisticsSettings.KILLSTREAK_TEXT,
                "   $highestKillstreak"
            )
        )
        pages.add(page2)

        return pages
    }

    private fun createPage(lines: List<String>) = lines.joinToString("\n")
}