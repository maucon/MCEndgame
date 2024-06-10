package de.fuballer.mcendgame.component.dungeon.remaining

import de.fuballer.mcendgame.component.dungeon.generation.DungeonGenerationSettings
import org.bukkit.ChatColor

object RemainingSettings {
    const val COMMAND_NAME = "dungeon-remaining"

    val GENERAL_ERROR = ChatColor.RED.toString() + "An error occurred"
    val NOT_IN_DUNGEON_ERROR = ChatColor.RED.toString() + "Currently not in a dungeon"

    fun getRemainingMessage(remainingMonster: Int, bossesSlain: Int, progressGranted: Boolean) =
        "${ChatColor.AQUA}$remainingMonster monsters remaining ${ChatColor.GRAY}|" +
                " ${ChatColor.LIGHT_PURPLE}$bossesSlain/${DungeonGenerationSettings.BOSS_AMOUNT} bosses slain ${ChatColor.GRAY}|" +
                " Completed: ${if (progressGranted) "${ChatColor.GREEN}✅" else "${ChatColor.RED}❎"}"
}