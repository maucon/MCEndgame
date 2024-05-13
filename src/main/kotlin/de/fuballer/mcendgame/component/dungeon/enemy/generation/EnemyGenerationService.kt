package de.fuballer.mcendgame.component.dungeon.enemy.generation

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import de.fuballer.mcendgame.component.dungeon.enemy.EnemyHealingService.Companion.heal
import de.fuballer.mcendgame.component.dungeon.enemy.equipment.EquipmentGenerationService
import de.fuballer.mcendgame.event.DungeonEnemySpawnedEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.EntityUtil
import de.fuballer.mcendgame.util.SchedulingUtil
import de.fuballer.mcendgame.util.random.RandomOption
import de.fuballer.mcendgame.util.random.RandomUtil
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Listener
import kotlin.random.Random

@Component
class EnemyGenerationService(
    private val equipmentGenerationService: EquipmentGenerationService
) : Listener {
    fun generate(
        random: Random,
        mapTier: Int,
        world: World,
        normalEntityTypes: List<RandomOption<CustomEntityType>>,
        normalEnemySpawnLocations: List<Location>
    ) {
        SchedulingUtil.runTask {
            val entities = normalEnemySpawnLocations
                .map { spawnEntity(random, mapTier, normalEntityTypes, it) }
                .toMutableSet()

            val event = DungeonEnemySpawnedEvent(world, entities)
            EventGateway.apply(event)
        }
    }

    fun addEffectsToEnemy(
        random: Random,
        entity: LivingEntity,
        mapTier: Int,
        canBeInvisible: Boolean
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

    private fun spawnEntity(
        random: Random,
        mapTier: Int,
        randomEntityTypes: List<RandomOption<CustomEntityType>>,
        location: Location
    ): LivingEntity {
        val entityType = RandomUtil.pick(randomEntityTypes, random).option
        val entity = EntityUtil.spawnCustomEntity(entityType, location, mapTier) as LivingEntity
        equipmentGenerationService.generate(random, entity, mapTier, entityType.canHaveWeapons, entityType.isRanged, entityType.canHaveArmor)

        entity.heal()

        val canBeInvisible = !entityType.hideEquipment
        addEffectsToEnemy(random, entity, mapTier, canBeInvisible)

        return entity
    }
}