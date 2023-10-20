package de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.summoner

import de.fuballer.mcendgame.component.dungeon.enemy.EnemyGenerationService
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.CustomEntityType
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.DataTypeKeys
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.summoner.db.MinionRepository
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.summoner.db.MinionsEntity
import de.fuballer.mcendgame.component.statitem.StatItemService
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.PersistentDataUtil
import org.bukkit.World
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Creature
import org.bukkit.entity.LivingEntity

@Component
class SummonerService(
    private val minionRepo: MinionRepository,
    private val statItemService: StatItemService,
    private val enemyGenerationService: EnemyGenerationService,
) {
    fun summonMinions(
        summoner: LivingEntity,
        minionType: CustomEntityType,
        amount: Int,
        weapons: Boolean,
        ranged: Boolean,
        armor: Boolean,
        health: Double,
    ) {
        val world = summoner.world
        val mapTier = PersistentDataUtil.getValue(summoner, DataTypeKeys.MAP_TIER) ?: -1

        val minions = mutableSetOf<LivingEntity>()
        for (i in 0 until amount) {
            minions.add(summonMinion(world, summoner, mapTier, minionType, weapons, ranged, armor, health))
        }

        if (!minionRepo.exists(summoner.uniqueId))
            minionRepo.save(MinionsEntity(summoner.uniqueId, minions))
        else
            minionRepo.getById(summoner.uniqueId).minions.addAll(minions)
    }

    private fun summonMinion(
        world: World,
        summoner: LivingEntity,
        mapTier: Int,
        minionType: CustomEntityType,
        weapons: Boolean,
        ranged: Boolean,
        armor: Boolean,
        health: Double,
    ): LivingEntity {
        val minion = world.spawnEntity(summoner.location, minionType.type) as LivingEntity

        setHealth(minion, health)

        PersistentDataUtil.setValue(minion, DataTypeKeys.IS_MINION, true)
        PersistentDataUtil.setValue(minion, DataTypeKeys.DROP_BASE_LOOT, false)

        if (mapTier < 0 || minion !is Creature) return minion

        statItemService.setCreatureEquipment(minion, mapTier, weapons, ranged, armor)
        enemyGenerationService.addEffectsToEntity(minion, mapTier)

        PersistentDataUtil.setValue(minion, DataTypeKeys.DROP_EQUIPMENT, false)

        return minion
    }

    private fun setHealth(entity: LivingEntity, health: Double) {
        val attributeInstance = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH) ?: return
        attributeInstance.baseValue = health
        entity.health = health
    }
}