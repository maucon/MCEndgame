package de.fuballer.mcendgame.component.dungeon.enemy.generation

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import de.fuballer.mcendgame.component.dungeon.enemy.equipment.EquipmentGenerationService
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
import kotlin.random.Random

@Component
class EnemyGenerationService(
    private val equipmentGenerationService: EquipmentGenerationService
) : Listener {
    @EventHandler
    fun on(event: EntityPotionEffectEvent) {
        if (event.cause != EntityPotionEffectEvent.Cause.EXPIRATION) return
        val effect = event.oldEffect ?: return
        if (effect.type != EnemyGenerationSettings.INIT_POTION_EFFECT_TYPE) return

        val entity = event.entity as? LivingEntity ?: return
        if (!entity.world.isDungeonWorld()) return

        entity.health = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
    }

    fun generate(
        random: Random,
        mapTier: Int,
        world: World,
        normalEntityTypes: List<RandomOption<CustomEntityType>>,
        normalEnemySpawnLocations: List<Location>,
        specialEntityTypes: List<RandomOption<CustomEntityType>>,
        specialEnemySpawnLocations: List<Location>
    ) {
        SchedulingUtil.runTask {
            val entities = normalEnemySpawnLocations
                .map { spawnEntity(random, mapTier, normalEntityTypes, it, special = false) }
                .toMutableSet()

            val specialEntities = spawnSpecialEntities(random, mapTier, specialEntityTypes, specialEnemySpawnLocations)
            entities.addAll(specialEntities)

            val event = DungeonEnemySpawnedEvent(world, entities)
            EventGateway.apply(event)
        }
    }

    fun addEffectsToEnemy(
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

    private fun spawnSpecialEntities(
        random: Random,
        mapTier: Int,
        specialEntityTypes: List<RandomOption<CustomEntityType>>,
        possibleLocations: List<Location>
    ): List<LivingEntity> {
        return possibleLocations.shuffled(random)
            .take(EnemyGenerationSettings.SPECIAL_MOB_COUNT)
            .map {
                spawnEntity(random, mapTier, specialEntityTypes, it, special = true)
            }
    }

    private fun spawnEntity(
        random: Random,
        mapTier: Int,
        randomEntityTypes: List<RandomOption<CustomEntityType>>,
        location: Location,
        special: Boolean = false
    ): LivingEntity {
        val entityType = RandomUtil.pick(randomEntityTypes, random).option
        val entity = EntityUtil.spawnCustomEntity(entityType, location, mapTier) as LivingEntity
        equipmentGenerationService.generate(random, entity, mapTier, entityType.canHaveWeapons, entityType.isRanged, entityType.canHaveArmor)

        addEffectUntilLoad(entity)

        val canBeInvisible = !entityType.hideEquipment
        addEffectsToEnemy(random, entity, mapTier, canBeInvisible)

        if (special) {
            entity.setIsSpecial()
        }

        return entity
    }

    private fun addEffectUntilLoad(entity: LivingEntity) {
        val effect = PotionEffect(EnemyGenerationSettings.INIT_POTION_EFFECT_TYPE, 1, 0, false, false)
        entity.addPotionEffect(effect)
    }
}