package de.fuballer.mcendgame.component.statistics.db

import de.fuballer.mcendgame.component.custom_entity.data.CustomEntityType
import de.fuballer.mcendgame.component.statistics.StatisticsSettings
import de.fuballer.mcendgame.framework.stereotype.Entity
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.BookMeta
import java.util.*

data class StatisticsEntity(
    override var id: UUID,

    var dungeonsCompleted: Int = 0,
    var highestCompletedDungeon: Int = 0,
    var dungeonsOpened: Int = 0,
    var deaths: Int = 0,

    var totalKills: Int = 0,
    var mobTypeKills: MutableMap<EntityType, Int> =
        CustomEntityType.entries
            .associate { it.type to 0 }
            .toMutableMap(),

    var damageDealt: Double = 0.0,
    var rawDamageDealt: Double = 0.0,
    var damageTaken: Double = 0.0,
    var rawDamageTaken: Double = 0.0,

    var highestKillstreak: Int = 0,
    var killstreakThresholdsReached: MutableMap<Int, Int> = mutableMapOf(
        25 to 0,
        50 to 0,
        75 to 0,
        100 to 0,
        200 to 0,
        300 to 0,
    )
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

        var page1 = "${StatisticsSettings.STATISTICS_BOOK_BASICS_HEADLINE}\n\n"
        page1 = page1.plus("${StatisticsSettings.DUNGEONS_COMPLETED_TEXT}\n   $dungeonsCompleted\n")
        page1 = page1.plus("${StatisticsSettings.HIGHEST_DUNGEONS_COMPLETED_TEXT}\n   $highestCompletedDungeon\n")
        page1 = page1.plus("${StatisticsSettings.DUNGEONS_OPENED_TEXT}\n   $dungeonsOpened\n")
        page1 = page1.plus("${StatisticsSettings.TOTAL_KILLS_TEXT}\n   $totalKills\n")
        page1 = page1.plus("${StatisticsSettings.DEATHS_TEXT}\n   $deaths\n")
        page1 = page1.plus("${StatisticsSettings.KILLSTREAK_TEXT}\n   $highestKillstreak\n")
        pages.add(page1)

        var page3 = ""
        for (mobType in mobTypeKills.keys) {
            val typeString = "${StatisticsSettings.STATISTICS_BOOK_MOB_TYPE_COLOR}${getMobTypeDisplayName(mobType)}:"
            val amountString = "${StatisticsSettings.STATISTICS_BOOK_TEXT_COLOR}${mobTypeKills[mobType]}"
            page3 = page3.plus("$typeString\n   $amountString\n")
        }
        pages.add(page3)

        var page4 = "${StatisticsSettings.STATISTICS_BOOK_DAMAGE_HEADLINE}\n\n"
        page4 = page4.plus("${StatisticsSettings.DAMAGE_DEALT_TEXT}\n   ${String.format("%.2f", damageDealt)}\n")
        page4 = page4.plus("${StatisticsSettings.RAW_DAMAGE_DEALT_TEXT}\n   ${String.format("%.2f", rawDamageDealt)}\n")
        page4 = page4.plus("${StatisticsSettings.DAMAGE_TAKEN_TEXT}\n   ${String.format("%.2f", damageTaken)}\n")
        page4 = page4.plus("${StatisticsSettings.RAW_DAMAGE_TAKEN_TEXT}\n   ${String.format("%.2f", rawDamageTaken)}\n")
        pages.add(page4)

        var page5 = "${StatisticsSettings.STATISTICS_BOOK_KILLSTREAK_HEADLINE}\n\n"
        for (killstreakThreshold in killstreakThresholdsReached.keys) {
            val thresholdString = "${StatisticsSettings.STATISTICS_BOOK_KILLSTREAK_THRESHOLD_COLOR}$killstreakThreshold:"
            val amountString = "${StatisticsSettings.STATISTICS_BOOK_TEXT_COLOR}${killstreakThresholdsReached[killstreakThreshold]}"
            page5 = page5.plus("$thresholdString\n   $amountString\n")
        }
        pages.add(page5)

        return pages
    }

    private fun getMobTypeDisplayName(mobType: EntityType): String {
        var displayName = ""

        val words = mobType.name.split("_")
        for (word in words) {
            val lowerCaseWord = word.lowercase()
            val uppercaseWord = lowerCaseWord[0].uppercase() + lowerCaseWord.substring(1)
            displayName = displayName.plus("$uppercaseWord ")
        }

        return displayName.trim()
    }
}
