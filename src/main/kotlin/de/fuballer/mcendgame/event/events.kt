package de.fuballer.mcendgame.event

import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.inventory.Recipe

/**
 * Thrown whenever a dungeon got completed (boss died)
 */
data class DungeonCompleteEvent(
    val mapTier: Int,
    val world: World
) : HandleableEvent()

/**
 * Thrown whenever a player opens a dungeon
 */
class DungeonOpenEvent(
    val player: Player,
    val dungeonWorld: World
) : HandleableEvent()

/**
 * Thrown whenever the killstreak of a dungeon increases
 */
class KillStreakIncreaseEvent(
    val killstreak: Int,
    val world: World
) : HandleableEvent()

/**
 * Thrown whenever a dungeon world gets deleted
 */
class DungeonWorldDeleteEvent(
    val world: World
) : HandleableEvent()

/**
 * Thrown whenever a player joins a dungeon
 */
class PlayerDungeonJoinEvent(
    val player: Player,
    val world: World,
    val locationToTeleport: Location
) : HandleableEvent()

/**
 * Thrown whenever a player leaves a dungeon
 */
class PlayerDungeonLeaveEvent(
    val player: Player
) : HandleableEvent()

/**
 * Thrown whenever a recipe is added to the discoverable recipes
 */
class DiscoverRecipeAddEvent(
    val key: NamespacedKey,
    val recipe: Recipe
) : HandleableEvent()

/**
 * Thrown whenever a recipe is added to the discoverable recipes
 *
 * @param world world mobs got spawned in
 * @param count count of spawned mobs
 */
class DungeonEnemySpawnedEvent(
    val world: World,
    val count: Int
) : HandleableEvent()
