package de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.summoner

import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.CustomEntityType
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.Keys
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.MinionRepository
import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.MinionsEntity
import de.fuballer.mcendgame.component.statitem.StatItemService
import org.bukkit.Bukkit
import org.bukkit.entity.Creature
import org.bukkit.entity.LivingEntity
import org.bukkit.persistence.PersistentDataType

class SummonerService(
    private val minionRepo: MinionRepository,
    private val statItemService: StatItemService,
) {
    fun summonMinions(
        summoner: LivingEntity,
        minionType: CustomEntityType,
        amount: Int,
        weapons: Boolean,
        armor: Boolean,
    ) {
        val world = summoner.world

        val minions = mutableSetOf<LivingEntity>()
        for (i in 0 until amount) {
            val minion = world.spawnEntity(summoner.location, minionType.type) as LivingEntity

            val mapTier = getMapTier(summoner)

            if (mapTier >= 0 && minion is Creature) {
                statItemService.setCreatureEquipment(minion, mapTier, weapons, armor)
                minion.persistentDataContainer.set(Keys.DROP_EQUIPMENT_KEY, PersistentDataType.BOOLEAN, false)
            }

            minion.persistentDataContainer.set(Keys.DROP_BASE_LOOT_KEY, PersistentDataType.BOOLEAN, false)

            minions.add(minion)
        }

        if (!minionRepo.exists(summoner.uniqueId))
            minionRepo.save(MinionsEntity(summoner.uniqueId, minions))
        else
            minionRepo.getById(summoner.uniqueId).minions.addAll(minions)
    }

    private fun getMapTier(entity: LivingEntity): Int {
        if (!entity.persistentDataContainer.has(Keys.MAP_TIER_KEY, PersistentDataType.INTEGER)) return -1
        return entity.persistentDataContainer.get(Keys.MAP_TIER_KEY, PersistentDataType.INTEGER) ?: return -1
    }
}