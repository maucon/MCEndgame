package de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.summoner

import de.fuballer.mcendgame.component.dungeon.enemy.EnemyGenerationService
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.CustomEntityType
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.Keys
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.MinionRepository
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.MinionsEntity
import de.fuballer.mcendgame.component.statitem.StatItemService
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.PersistentDataUtil
import org.bukkit.World
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Creature
import org.bukkit.entity.LivingEntity
import org.bukkit.persistence.PersistentDataType

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
        val mapTier = PersistentDataUtil.getValue(summoner.persistentDataContainer, Keys.MAP_TIER, PersistentDataType.INTEGER) ?: -1

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

        minion.persistentDataContainer.set(Keys.IS_MINION, PersistentDataType.BOOLEAN, true)
        minion.persistentDataContainer.set(Keys.DROP_BASE_LOOT, PersistentDataType.BOOLEAN, false)

        if (mapTier < 0 || minion !is Creature) return minion

        statItemService.setCreatureEquipment(minion, mapTier, weapons, ranged, armor)
        enemyGenerationService.addEffectsToEntity(minion, mapTier)
        minion.persistentDataContainer.set(Keys.DROP_EQUIPMENT, PersistentDataType.BOOLEAN, false)

        return minion
    }

    private fun setHealth(entity: LivingEntity, health: Double) {
        val attributeInstance = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH) ?: return
        attributeInstance.baseValue = health
        entity.health = health
    }
}