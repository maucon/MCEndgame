package de.fuballer.mcendgame.component.dungeon.enemy.generation

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import de.fuballer.mcendgame.component.dungeon.enemy.EnemyHealingService.Companion.healOnLoad
import de.fuballer.mcendgame.component.dungeon.enemy.equipment.EquipmentGenerationService
import de.fuballer.mcendgame.event.DungeonEnemySpawnedEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.EntityUtil
import de.fuballer.mcendgame.util.ThreadUtil.bukkitSync
import de.fuballer.mcendgame.util.extension.EntityExtension.setIsElite
import de.fuballer.mcendgame.util.extension.EntityExtension.setIsLootGoblin
import de.fuballer.mcendgame.util.random.RandomOption
import de.fuballer.mcendgame.util.random.RandomUtil
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.event.Listener
import kotlin.random.Random

@Service
class EnemyGenerationService(
    private val equipmentGenerationService: EquipmentGenerationService
) : Listener {
    fun generate(
        random: Random,
        mapTier: Int,
        world: World,
        normalEntityTypes: List<RandomOption<CustomEntityType>>,
        normalEnemySpawnLocations: List<Location>
    ) = bukkitSync {
        val entities = normalEnemySpawnLocations
            .map { spawnEntity(random, mapTier, normalEntityTypes, it) }
            .toMutableSet()

        val event = DungeonEnemySpawnedEvent(world, entities)
        EventGateway.apply(event)
    }

    private fun spawnEntity(
        random: Random,
        mapTier: Int,
        randomEntityTypes: List<RandomOption<CustomEntityType>>,
        location: Location
    ): LivingEntity {
        val entityType = RandomUtil.pick(randomEntityTypes, random).option
        val entity = EntityUtil.spawnCustomEntity(entityType, location, mapTier) as LivingEntity

        val isLootGoblin = isLootGoblin(entity, entityType, random)
        val isElite = isElite(entity, random)
        setEntityScale(entity, isElite, random)

        val canBeInvisible = !entityType.hideEquipment && !isElite
        addEffectsToEnemy(random, entity, mapTier, canBeInvisible)

        equipmentGenerationService.generate(random, entity, mapTier, entityType.canHaveWeapons, entityType.isRanged, entityType.canHaveArmor, isLootGoblin)

        entity.healOnLoad()

        return entity
    }

    private fun isLootGoblin(entity: LivingEntity, type: CustomEntityType, random: Random): Boolean {
        if (type.hideEquipment) return false
        if (random.nextDouble() > EnemyGenerationSettings.LOOT_GOBLIN_CHANCE) return false

        entity.setIsLootGoblin()
        return true
    }

    private fun isElite(entity: LivingEntity, random: Random): Boolean {
        if (random.nextDouble() > EnemyGenerationSettings.ELITE_CHANCE) return false
        entity.setIsElite()

        val healthAttribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)
        val newMaxHealth = healthAttribute?.baseValue!! * EnemyGenerationSettings.ELITE_HEALTH_FACTOR
        healthAttribute.baseValue = newMaxHealth

        entity.addPotionEffect(EnemyGenerationSettings.ELITE_POTION_EFFECT)

        return true
    }

    private fun setEntityScale(entity: LivingEntity, isElite: Boolean, random: Random) {
        val scaleAttribute = entity.getAttribute(Attribute.GENERIC_SCALE)
        val scale = if (isElite) EnemyGenerationSettings.ELITE_SCALE else EnemyGenerationSettings.getRandomScale(random)

        scaleAttribute?.baseValue = scale
    }

    private fun addEffectsToEnemy(
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
            RandomUtil.pick(EnemyGenerationSettings.ON_DEATH_EFFECTS, random).option,
        )
        if (canBeInvisible) {
            effects.add(RandomUtil.pick(EnemyGenerationSettings.INVISIBILITY_EFFECT, random).option)
        }

        val potionEffects = effects.filterNotNull()
            .map { it.getPotionEffect() }

        bukkitSync { entity.addPotionEffects(potionEffects) }
    }
}