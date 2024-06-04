package de.fuballer.mcendgame.event

import de.fuballer.mcendgame.component.portal.db.Portal
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.World
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe

/**
 * Thrown whenever a dungeon got completed (boss died)
 */
data class DungeonCompleteEvent(
    val player: Player,
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
 * Thrown whenever the killstreak is created for a player.
 */
class KillStreakCreatedEvent(
    val streak: Int,
    val player: Player
) : HandleableEvent()

/**
 * Thrown whenever the killstreak of players updated
 */
class KillStreakUpdatedEvent(
    val streak: Int,
    val players: List<Player>
) : HandleableEvent()

/**
 * Thrown whenever the killstreak of a player is removed.
 * (Does not mean the killstreak expired)
 */
class KillStreakRemovedEvent(
    val player: Player
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
 * Thrown whenever enemies are spawned in a dungeon
 *
 * @param world the world mobs got spawned in
 * @param entities the spawned entities
 */
class DungeonEnemySpawnedEvent(
    val world: World,
    val entities: Set<LivingEntity>
) : HandleableEvent()

/**
 * Thrown whenever an entity dies inside a dungeon
 */
class DungeonEntityDeathEvent(
    val entity: LivingEntity,
    val drops: MutableList<ItemStack>
) : HandleableEvent()

/**
 * Thrown whenever a player uses a portal
 */
class PortalUsedEvent(
    val portal: Portal,
    val player: Player
) : HandleableEvent()

/**
 * Thrown whenever a portal fails to teleport a player
 */
class PortalFailedEvent(
    val portal: Portal,
    val player: Player
) : HandleableEvent()

/**
 * Thrown whenever a dungeon gets generated
 */
class DungeonGeneratedEvent(
    val world: World,
    val respawnLocation: Location
) : HandleableEvent()