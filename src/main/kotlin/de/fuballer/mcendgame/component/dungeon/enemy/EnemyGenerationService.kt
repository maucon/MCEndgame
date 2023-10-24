package de.fuballer.mcendgame.component.dungeon.enemy

import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.data.CustomEntityType
import de.fuballer.mcendgame.component.dungeon.generation.DungeonGenerationSettings
import de.fuballer.mcendgame.component.dungeon.generation.data.LayoutTile
import de.fuballer.mcendgame.component.statitem.StatItemService
import de.fuballer.mcendgame.event.DungeonEnemySpawnedEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.PluginUtil
import de.fuballer.mcendgame.util.WorldUtil
import de.fuballer.mcendgame.util.random.RandomUtil
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Creature
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.awt.Point
import java.util.*

@Component
class EnemyGenerationService(
    private val statItemService: StatItemService
) : Listener {
    private val random = Random()

    @EventHandler
    fun onEntityPotionEffect(event: EntityPotionEffectEvent) {
        if (event.cause != EntityPotionEffectEvent.Cause.EXPIRATION) return
        val effect = event.oldEffect ?: return
        if (effect.type != PotionEffectType.LUCK) return

        val entity = event.entity as? LivingEntity ?: return
        if (WorldUtil.isNotDungeonWorld(entity.world)) return

        entity.health = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value
    }

    fun summonMonsters(
        layoutTiles: Array<Array<LayoutTile>>,
        startPoint: Point,
        mapTier: Int,
        world: World
    ) {
        for (x in layoutTiles.indices) {
            for (y in layoutTiles[0].indices) {

                if (startPoint.x == x && startPoint.y == y) continue

                PluginUtil.scheduleTask {
                    val mobCount = EnemyGenerationSettings.calculateMobCount(random)
                    spawnMobs(mobCount, -x * 16.0 - 8, -y * 16.0 - 8, mapTier, world)
                }
            }
        }
    }

    private fun spawnMobs(
        amount: Int,
        x: Double,
        z: Double,
        mapTier: Int,
        world: World
    ) {
        val entities = mutableSetOf<LivingEntity>()
        for (i in 0 until amount) {
            val entityType = RandomUtil.pick(EnemyGenerationSettings.DUNGEON_MOBS).option
            val entity = CustomEntityType.spawnCustomEntity(
                entityType,
                Location(
                    world,
                    x + EnemyGenerationSettings.MOB_XZ_SPREAD * (random.nextDouble() * 2 - 1),
                    DungeonGenerationSettings.MOB_Y_POS,
                    z + EnemyGenerationSettings.MOB_XZ_SPREAD * (random.nextDouble() * 2 - 1)
                ),
                mapTier
            ) as Creature

            statItemService.setCreatureEquipment(entity, mapTier, entityType.data.canHaveWeapons, entityType.data.isRanged, entityType.data.canHaveArmor)

            addEffectUntilLoad(entity)
            addTemporarySlowfalling(entity)
            val canBeInvisible = !entityType.data.hideEquipment
            addEffectsToEntity(entity, mapTier, canBeInvisible)

            entities.add(entity)
        }

        val event = DungeonEnemySpawnedEvent(world, entities)
        EventGateway.apply(event)
    }

    private fun addEffectUntilLoad(entity: Creature) {
        val effect = PotionEffect(PotionEffectType.LUCK, 1, 0, false, false)
        entity.addPotionEffect(effect)
    }

    private fun addTemporarySlowfalling(entity: Creature) {
        val effect = PotionEffect(PotionEffectType.SLOW_FALLING, 40, 0, false, false)
        entity.addPotionEffect(effect)
    }

    fun addEffectsToEntity(
        entity: Creature,
        mapTier: Int,
        canBeInvisible: Boolean,
    ) {
        val effects = mutableListOf(
            RandomUtil.pick(EnemyGenerationSettings.STRENGTH_EFFECTS, mapTier).option,
            RandomUtil.pick(EnemyGenerationSettings.RESISTANCE_EFFECTS, mapTier).option,
            RandomUtil.pick(EnemyGenerationSettings.SPEED_EFFECTS, mapTier).option,
            RandomUtil.pick(EnemyGenerationSettings.FIRE_RESISTANCE_EFFECT, mapTier).option,
        )
        if (canBeInvisible) {
            effects.add(RandomUtil.pick(EnemyGenerationSettings.INVISIBILITY_EFFECT).option)
        }

        val potionEffects = effects.filterNotNull().map { it.getPotionEffect() }

        entity.addPotionEffects(potionEffects)
    }
}
