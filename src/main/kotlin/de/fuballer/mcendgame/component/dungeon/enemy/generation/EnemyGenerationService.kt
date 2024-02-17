package de.fuballer.mcendgame.component.dungeon.enemy.generation

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import de.fuballer.mcendgame.component.dungeon.enemy.equipment.EquipmentGenerationService
import de.fuballer.mcendgame.component.dungeon.generation.DungeonGenerationSettings
import de.fuballer.mcendgame.component.dungeon.generation.data.LayoutTile
import de.fuballer.mcendgame.event.DungeonEnemySpawnedEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.EntityUtil
import de.fuballer.mcendgame.util.SchedulingUtil
import de.fuballer.mcendgame.util.extension.EntityExtension.setIsSpecial
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import de.fuballer.mcendgame.util.random.RandomOption
import de.fuballer.mcendgame.util.random.RandomUtil
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.awt.Point
import kotlin.random.Random

@Component
class EnemyGenerationService(
    private val equipmentGenerationService: EquipmentGenerationService
) : Listener {
    @EventHandler
    fun on(event: EntityPotionEffectEvent) {
        if (event.cause != EntityPotionEffectEvent.Cause.EXPIRATION) return
        val effect = event.oldEffect ?: return
        if (effect.type != PotionEffectType.LUCK) return

        val entity = event.entity as? LivingEntity ?: return
        if (!entity.world.isDungeonWorld()) return

        entity.health = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
    }

    fun summonMonsters(
        random: Random,
        randomEntityTypes: List<RandomOption<CustomEntityType>>,
        specialEntityTypes: List<RandomOption<CustomEntityType>>,
        layoutTiles: Array<Array<LayoutTile>>,
        startPoint: Point,
        mapTier: Int,
        world: World
    ) {
        val tileList = getSpawnableTiles(layoutTiles, startPoint)
            .onEach {
                SchedulingUtil.runTask {
                    val mobCount = EnemyGenerationSettings.generateMobCount(random)
                    spawnMobs(random, randomEntityTypes, mobCount, -it.x * 16.0 - 8, -it.y * 16.0 - 8, mapTier, world)
                }
            }

        spawnSpecialMonster(random, tileList, specialEntityTypes, mapTier, world)
    }

    private fun getSpawnableTiles(
        layoutTiles: Array<Array<LayoutTile>>,
        startPoint: Point
    ) = layoutTiles.indices
        .flatMap { x ->
            layoutTiles[0].indices.map { y ->
                Point(x, y)
            }
        }
        .filter { startPoint.x != it.x || startPoint.y != it.y }

    private fun spawnSpecialMonster(
        random: Random,
        tileList: List<Point>,
        specialEntityTypes: List<RandomOption<CustomEntityType>>,
        mapTier: Int,
        world: World
    ) {
        tileList.shuffled(random)
            .take(EnemyGenerationSettings.SPECIAL_MOB_COUNT)
            .forEach {
                SchedulingUtil.runTask {
                    spawnMobs(random, specialEntityTypes, 1, -it.x * 16.0 - 8, -it.y * 16.0 - 8, mapTier, world, special = true)
                }
            }
    }

    private fun spawnMobs(
        random: Random,
        randomEntityTypes: List<RandomOption<CustomEntityType>>,
        amount: Int,
        x: Double,
        z: Double,
        mapTier: Int,
        world: World,
        special: Boolean = false
    ) {
        val spawnedEntities = mutableSetOf<LivingEntity>()

        for (i in 0 until amount) {
            val entityType = RandomUtil.pick(randomEntityTypes, random).option
            val spawnLocation = Location(
                world,
                x + EnemyGenerationSettings.MOB_XZ_SPREAD * (random.nextDouble() * 2 - 1),
                DungeonGenerationSettings.MOB_Y_POS,
                z + EnemyGenerationSettings.MOB_XZ_SPREAD * (random.nextDouble() * 2 - 1)
            )

            val entity = EntityUtil.spawnCustomEntity(entityType, spawnLocation, mapTier) as LivingEntity
            equipmentGenerationService.setCreatureEquipment(random, entity, mapTier, entityType.canHaveWeapons, entityType.isRanged, entityType.canHaveArmor)

            addEffectUntilLoad(entity)
            addTemporarySlowfalling(entity)

            val canBeInvisible = !entityType.hideEquipment
            addEffectsToEntity(random, entity, mapTier, canBeInvisible)

            if (special) entity.setIsSpecial()

            spawnedEntities.add(entity)
        }

        val event = DungeonEnemySpawnedEvent(world, spawnedEntities)
        EventGateway.apply(event)
    }

    private fun addEffectUntilLoad(entity: LivingEntity) {
        val effect = PotionEffect(PotionEffectType.LUCK, 1, 0, false, false)
        entity.addPotionEffect(effect)
    }

    private fun addTemporarySlowfalling(entity: LivingEntity) {
        val effect = PotionEffect(PotionEffectType.SLOW_FALLING, 40, 0, false, false)
        entity.addPotionEffect(effect)
    }

    fun addEffectsToEntity(
        random: Random,
        entity: LivingEntity,
        mapTier: Int,
        canBeInvisible: Boolean,
    ) {
        val effects = mutableListOf(
            RandomUtil.pick(EnemyGenerationSettings.STRENGTH_EFFECTS, mapTier, random).option,
            RandomUtil.pick(EnemyGenerationSettings.RESISTANCE_EFFECTS, mapTier, random).option,
            RandomUtil.pick(EnemyGenerationSettings.SPEED_EFFECTS, mapTier, random).option,
            RandomUtil.pick(EnemyGenerationSettings.FIRE_RESISTANCE_EFFECT, mapTier, random).option,
        )
        if (canBeInvisible) {
            effects.add(RandomUtil.pick(EnemyGenerationSettings.INVISIBILITY_EFFECT, random).option)
        }

        val potionEffects = effects.filterNotNull()
            .map { it.getPotionEffect() }

        entity.addPotionEffects(potionEffects)
    }
}