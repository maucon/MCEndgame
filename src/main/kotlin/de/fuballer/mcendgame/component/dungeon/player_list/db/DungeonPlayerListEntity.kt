package de.fuballer.mcendgame.component.dungeon.player_list.db

import de.fuballer.mcendgame.framework.stereotype.Entity
import org.bukkit.World
import org.bukkit.entity.Player

data class DungeonPlayerListEntity(
    override var id: World, // dungeon world
    val seed: Long,
    val tier: Int,
    val playerStats: MutableMap<Player, PlayerStats> = mutableMapOf(),
    val remainingData: RemainingData = RemainingData()
) : Entity<World>

data class PlayerStats(
    var damageDealt: Double = 0.0,
    var damageTaken: Double = 0.0,
    var kills: Int = 0
)

data class RemainingData(
    var remaining: Int = 0,
    var bossesSlain: Int = 0,
    var dungeonCompleted: Boolean = false
)
