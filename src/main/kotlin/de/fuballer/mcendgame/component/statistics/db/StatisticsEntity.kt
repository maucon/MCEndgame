package de.fuballer.mcendgame.component.statistics.db

import de.fuballer.mcendgame.component.statistics.StatisticsSettings
import de.fuballer.mcendgame.framework.stereotype.Entity
import de.fuballer.mcendgame.util.TextComponent
import net.kyori.adventure.text.Component
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
    var eliteKills: Int = 0,
    var deaths: Int = 0,
) : Entity<UUID> {
    fun createBook(): ItemStack {
        val book = ItemStack(Material.WRITTEN_BOOK)
        val bookMeta = book.itemMeta as BookMeta

        bookMeta.author = StatisticsSettings.STATISTICS_BOOK_AUTHOR
        bookMeta.title = StatisticsSettings.STATISTICS_BOOK_TITLE

        val pages = getPages()
        bookMeta.addPages(*pages)

        book.itemMeta = bookMeta

        return book
    }

    private fun getPages(): Array<Component> {
        val pages = mutableListOf<Component>()

        val page1 = Component.textOfChildren(
            StatisticsSettings.STATISTICS_BOOK_DUNGEONS_HEADLINE,
            TextComponent.empty(),
            StatisticsSettings.DUNGEONS_OPENED_TEXT,
            TextComponent.create("   $dungeonsOpened", StatisticsSettings.STATISTICS_BOOK_TEXT_COLOR),
            StatisticsSettings.DUNGEONS_COMPLETED_TEXT,
            TextComponent.create("   $dungeonsCompleted", StatisticsSettings.STATISTICS_BOOK_TEXT_COLOR),
            StatisticsSettings.HIGHEST_DUNGEONS_COMPLETED_TEXT,
            TextComponent.create("   $highestCompletedDungeon", StatisticsSettings.STATISTICS_BOOK_TEXT_COLOR),
        )
        pages.add(page1)

        val page2 = Component.textOfChildren(
            StatisticsSettings.STATISTICS_BOOK_COMBAT_HEADLINE,
            TextComponent.empty(),
            StatisticsSettings.TOTAL_KILLS_TEXT,
            TextComponent.create("   $totalKills", StatisticsSettings.STATISTICS_BOOK_TEXT_COLOR),
            StatisticsSettings.BOSS_KILLS_TEXT,
            TextComponent.create("   $bossKills", StatisticsSettings.STATISTICS_BOOK_TEXT_COLOR),
            StatisticsSettings.LOOT_GOBLINS_KILLS_TEXT,
            TextComponent.create("   $lootGoblinKills", StatisticsSettings.STATISTICS_BOOK_TEXT_COLOR),
            StatisticsSettings.ELITE_KILLS_TEXT,
            TextComponent.create("   $eliteKills", StatisticsSettings.STATISTICS_BOOK_TEXT_COLOR),
            StatisticsSettings.DEATHS_TEXT,
            TextComponent.create("   $deaths", StatisticsSettings.STATISTICS_BOOK_TEXT_COLOR),
        )
        pages.add(page2)

        return pages.toTypedArray()
    }
}