package de.fuballer.mcendgame.component.dungeon.player_list

import de.fuballer.mcendgame.component.dungeon.generation.DungeonGenerationSettings
import de.fuballer.mcendgame.component.dungeon.player_list.db.DungeonPlayerListEntity
import de.fuballer.mcendgame.component.dungeon.player_list.db.PlayerStats
import de.fuballer.mcendgame.component.dungeon.player_list.db.RemainingData
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import java.text.DecimalFormat

private val FORMAT = DecimalFormat("#.##")

object DungeonPlayerListSettings {
    fun getFooter(entity: DungeonPlayerListEntity): String = "\n" +
            "Tier: ${entity.tier}\n" +
            "${getRemainingMessage(entity.remainingData)}\n" +
            "${ChatColor.DARK_GRAY}Seed: ${entity.seed}"

    fun getPlayerName(player: Player, playerStats: PlayerStats) =
        "${player.name} " +
                "${ChatColor.RESET}| ⚔${ChatColor.GREEN} ${FORMAT.format(playerStats.damageDealt)} " +
                "${ChatColor.RESET}| 🛡${ChatColor.RED} ${FORMAT.format(playerStats.damageTaken)} " +
                "${ChatColor.RESET}| 💀${ChatColor.BLUE} ${playerStats.kills}"

    private fun getRemainingMessage(data: RemainingData) =
        "${ChatColor.AQUA}${data.remaining} Monsters remaining\n" +
                "${ChatColor.LIGHT_PURPLE}${data.bossesSlain}/${DungeonGenerationSettings.BOSS_AMOUNT} Bosses slain\n" +
                "${ChatColor.GRAY}Completed: ${if (data.dungeonCompleted) "${ChatColor.GREEN}✅" else "${ChatColor.RED}❎"}"
}