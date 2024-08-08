package de.fuballer.mcendgame.component.dungeon.player_list

import de.fuballer.mcendgame.component.dungeon.generation.DungeonGenerationSettings
import de.fuballer.mcendgame.component.dungeon.player_list.db.DungeonPlayerListEntity
import de.fuballer.mcendgame.component.dungeon.player_list.db.PlayerStats
import de.fuballer.mcendgame.component.dungeon.player_list.db.RemainingData
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player
import java.text.DecimalFormat

private val FORMAT = DecimalFormat("#.##")

object DungeonPlayerListSettings {
    fun getFooter(entity: DungeonPlayerListEntity) =
        Component.newline()
            .append(Component.text("Tier: ${entity.tier}")).appendNewline()
            .append(getRemainingMessage(entity.remainingData)).appendNewline()
            .append(Component.text("Seed: ${entity.seed}").color(NamedTextColor.DARK_GRAY))

    fun getPlayerNameComponent(player: Player, playerStats: PlayerStats) =
        Component.text(player.name)
            .append(Component.textOfChildren(Component.text(" | "), Component.text("⚔ ${FORMAT.format(playerStats.damageDealt)}").color(NamedTextColor.GREEN)))
            .append(Component.textOfChildren(Component.text(" | "), Component.text("🛡 ${FORMAT.format(playerStats.damageTaken)}").color(NamedTextColor.RED)))
            .append(Component.textOfChildren(Component.text(" | "), Component.text("💀 ${playerStats.kills}").color(NamedTextColor.BLUE)))

    private fun getRemainingMessage(data: RemainingData) =
        Component.text("${data.remaining} Monsters remaining").color(NamedTextColor.AQUA).appendNewline()
            .append(Component.text("${data.bossesSlain}/${DungeonGenerationSettings.BOSS_AMOUNT} Bosses slain").color(NamedTextColor.LIGHT_PURPLE)).appendNewline()
            .append(Component.textOfChildren(Component.text("Completed: ").color(NamedTextColor.GRAY), getDungeonCompletedComponent(data.dungeonCompleted)))

    private fun getDungeonCompletedComponent(dungeonCompleted: Boolean) =
        if (dungeonCompleted)
            Component.text("✅").color(NamedTextColor.GREEN)
        else
            Component.text("❎").color(NamedTextColor.RED)
}