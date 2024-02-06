package de.fuballer.mcendgame.event

import de.fuballer.mcendgame.domain.attribute.AttributeType
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

class DamageCalculationEvent(
    val player: Player,
    val customPlayerAttributes: Map<AttributeType, List<Double>>,
    val damaged: LivingEntity,
    val baseDamage: MutableList<Double> = mutableListOf(),
    val increasedDamage: MutableList<Double> = mutableListOf(),
    val moreDamage: MutableList<Double> = mutableListOf(),
    var isProjectile: Boolean = false,
    var isCritical: Boolean = false,
    var attackCooldown: Double = 1.0,
    var nullifyDamage: Boolean = false
) : HandleableEvent()