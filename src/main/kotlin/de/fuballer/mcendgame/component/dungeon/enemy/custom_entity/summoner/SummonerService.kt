package de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.summoner

import de.fuballer.mcendgame.component.dungeon.enemy.EnemyGenerationService
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.data.CustomEntityType
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.data.DataTypeKeys
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.summoner.db.MinionRepository
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.summoner.db.MinionsEntity
import de.fuballer.mcendgame.component.statitem.StatItemService
import de.fuballer.mcendgame.event.DungeonEnemySpawnedEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.PersistentDataUtil
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Creature
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityTargetEvent

@Component
class SummonerService(
    private val minionRepo: MinionRepository,
    private val statItemService: StatItemService,
    private val enemyGenerationService: EnemyGenerationService
) {
    @EventHandler
    fun onEntityTarget(event: EntityTargetEvent) {
        val summoner = event.entity as? Creature ?: return
        if (!minionRepo.exists(summoner.uniqueId)) return

        setMinionsTarget(summoner)
    }

    private fun setMinionsTarget(summoner: Creature) {
        val target = summoner.target ?: return

        minionRepo.getById(summoner.uniqueId).minions.forEach {
            if (it is Creature)
                it.target = target
        }
    }

    fun summonMinions(
        summoner: Creature,
        minionType: CustomEntityType,
        amount: Int,
        weapons: Boolean,
        ranged: Boolean,
        armor: Boolean,
        health: Double,
    ) {
        val mapTier = PersistentDataUtil.getValue(summoner, DataTypeKeys.MAP_TIER) ?: -1

        val minions = mutableSetOf<LivingEntity>()
        for (i in 0 until amount) {
            minions.add(summonMinion(summoner, mapTier, minionType, weapons, ranged, armor, health))
        }

        val event = DungeonEnemySpawnedEvent(summoner.world, minions)
        EventGateway.apply(event)

        if (!minionRepo.exists(summoner.uniqueId))
            minionRepo.save(MinionsEntity(summoner.uniqueId, minions))
        else
            minionRepo.getById(summoner.uniqueId).minions.addAll(minions)

        setMinionsTarget(summoner)
    }

    private fun summonMinion(
        summoner: LivingEntity,
        mapTier: Int,
        minionType: CustomEntityType,
        weapons: Boolean,
        ranged: Boolean,
        armor: Boolean,
        health: Double,
    ): LivingEntity {
        val minion = CustomEntityType.spawnCustomEntity(minionType, summoner.location, mapTier) as LivingEntity

        setHealth(minion, health)

        PersistentDataUtil.setValue(minion, DataTypeKeys.IS_MINION, true)
        PersistentDataUtil.setValue(minion, DataTypeKeys.DROP_BASE_LOOT, false)

        if (mapTier < 0 || minion !is Creature) return minion

        statItemService.setCreatureEquipment(minion, mapTier, weapons, ranged, armor)
        val canBeInvisible = !minionType.data.hideEquipment
        enemyGenerationService.addEffectsToEntity(minion, mapTier, canBeInvisible)

        PersistentDataUtil.setValue(minion, DataTypeKeys.DROP_EQUIPMENT, false)

        return minion
    }

    private fun setHealth(entity: LivingEntity, health: Double) {
        val attributeInstance = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH) ?: return
        attributeInstance.baseValue = health
        entity.health = health
    }
}