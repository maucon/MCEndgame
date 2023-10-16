package de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.summoner

import de.fuballer.mcendgame.component.dungeon.enemy.EnemyGenerationService
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.CustomEntityType
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.Keys
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.MinionRepository
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.MinionsEntity
import de.fuballer.mcendgame.component.statitem.StatItemService
import org.bukkit.World
import org.bukkit.entity.Creature
import org.bukkit.entity.LivingEntity
import org.bukkit.persistence.PersistentDataType

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
    ) {
        val world = summoner.world
        val mapTier = getMapTier(summoner)

        val minions = mutableSetOf<LivingEntity>()
        for (i in 0 until amount) {
            minions.add(summonMinion(world, summoner, mapTier, minionType, weapons, ranged, armor))
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
    ): LivingEntity {
        val minion = world.spawnEntity(summoner.location, minionType.type) as LivingEntity

        minion.persistentDataContainer.set(Keys.IS_MINION, PersistentDataType.BOOLEAN, true)
        minion.persistentDataContainer.set(Keys.DROP_BASE_LOOT, PersistentDataType.BOOLEAN, false)

        if (mapTier < 0 || minion !is Creature) return minion

        statItemService.setCreatureEquipment(minion, mapTier, weapons, ranged, armor)
        enemyGenerationService.addEffectsToEntity(minion, mapTier)
        minion.persistentDataContainer.set(Keys.DROP_EQUIPMENT, PersistentDataType.BOOLEAN, false)

        return minion
    }

    private fun getMapTier(entity: LivingEntity): Int {
        if (!entity.persistentDataContainer.has(Keys.MAP_TIER, PersistentDataType.INTEGER)) return -1
        return entity.persistentDataContainer.get(Keys.MAP_TIER, PersistentDataType.INTEGER) ?: return -1
    }
}