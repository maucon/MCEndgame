package de.fuballer.mcendgame.component.custom_entity.summoner

import de.fuballer.mcendgame.component.dungeon.enemy.generation.EnemyGenerationService
import de.fuballer.mcendgame.component.item_generation.ItemGenerationService
import de.fuballer.mcendgame.domain.entity.CustomEntityType
import de.fuballer.mcendgame.domain.persistent_data.DataTypeKeys
import de.fuballer.mcendgame.event.DungeonEnemySpawnedEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.EntityUtil
import de.fuballer.mcendgame.util.PersistentDataUtil
import de.fuballer.mcendgame.util.SummonerUtil
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Creature
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.util.Vector
import java.util.*

@Component
class SummonerService(
    private val itemGenerationService: ItemGenerationService,
    private val enemyGenerationService: EnemyGenerationService
) {
    @EventHandler
    fun onEntityTarget(event: EntityTargetEvent) {
        val summoner = event.entity as? Creature ?: return
        val minions = PersistentDataUtil.getValue(summoner, DataTypeKeys.MINIONS) ?: return

        setMinionsTarget(summoner, minions)
    }

    private fun setMinionsTarget(summoner: Creature, minions: Set<UUID>) {
        val target = summoner.target ?: return
        val world = summoner.world

        WorldUtil.getFilteredEntities(world, minions, Creature::class)
            .forEach { it.target = target }
    }

    fun summonMinions(
        summoner: Creature,
        minionType: CustomEntityType,
        amount: Int,
        weapons: Boolean,
        ranged: Boolean,
        armor: Boolean,
        health: Double,
        spawnOffset: Vector,
    ) {
        val mapTier = PersistentDataUtil.getValue(summoner, DataTypeKeys.MAP_TIER) ?: -1

        val minions = mutableSetOf<LivingEntity>()
        for (i in 0 until amount) {
            minions.add(summonMinion(summoner, mapTier, minionType, weapons, ranged, armor, health, spawnOffset))
        }

        val minionIds = minions.map { it.uniqueId }
            .toMutableSet()

        SummonerUtil.addMinions(summoner, minions)

        val event = DungeonEnemySpawnedEvent(summoner.world, minions)
        EventGateway.apply(event)

        setMinionsTarget(summoner, minionIds)
    }

    private fun summonMinion(
        summoner: LivingEntity,
        mapTier: Int,
        minionType: CustomEntityType,
        weapons: Boolean,
        ranged: Boolean,
        armor: Boolean,
        health: Double,
        spawnOffset: Vector,
    ): LivingEntity {
        val minion = EntityUtil.spawnCustomEntity(minionType, summoner.location.add(spawnOffset), mapTier) as LivingEntity
        setHealth(minion, health)

        PersistentDataUtil.setValue(minion, DataTypeKeys.IS_MINION, true)
        PersistentDataUtil.setValue(minion, DataTypeKeys.DROP_EQUIPMENT, false)

        if (mapTier < 0 || minion !is Creature) return minion

        itemGenerationService.setCreatureEquipment(minion, mapTier, weapons, ranged, armor)
        val canBeInvisible = !minionType.hideEquipment
        enemyGenerationService.addEffectsToEntity(minion, mapTier, canBeInvisible)

        return minion
    }

    private fun setHealth(entity: LivingEntity, health: Double) {
        val attributeInstance = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH) ?: return
        attributeInstance.baseValue = health
        entity.health = health
    }
}