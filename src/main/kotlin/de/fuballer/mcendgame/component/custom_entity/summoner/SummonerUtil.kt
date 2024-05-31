package de.fuballer.mcendgame.component.custom_entity.summoner

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import de.fuballer.mcendgame.event.DungeonEnemySpawnedEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.util.EntityUtil
import de.fuballer.mcendgame.util.WorldUtil
import de.fuballer.mcendgame.util.extension.EntityExtension.getMapTier
import de.fuballer.mcendgame.util.extension.EntityExtension.getMinionIds
import de.fuballer.mcendgame.util.extension.EntityExtension.setDisableDropEquipment
import de.fuballer.mcendgame.util.extension.EntityExtension.setIsEnemy
import de.fuballer.mcendgame.util.extension.EntityExtension.setIsMinion
import de.fuballer.mcendgame.util.extension.EntityExtension.setMinionIds
import org.bukkit.Location
import org.bukkit.entity.Creature
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity

object SummonerUtil {
    fun summonMinions(
        summoner: Creature,
        minionType: CustomEntityType,
        amount: Int,
        spawnLocation: Location,
    ): Set<LivingEntity> {
        val mapTier = summoner.getMapTier() ?: -1

        val minions = (0 until amount)
            .map { summonMinion(mapTier, minionType, spawnLocation) }
            .toSet()

        addMinions(summoner, minions)
        setMinionsTarget(summoner, minions)

        val event = DungeonEnemySpawnedEvent(summoner.world, minions)
        EventGateway.apply(event)

        return minions
    }

    fun getMinionEntities(summoner: Entity): List<Creature> {
        val world = summoner.world
        val minionIds = summoner.getMinionIds()

        val aliveMinions = WorldUtil.getFilteredEntities(world, minionIds, Creature::class)
            .filter { !it.isDead }
        val aliveMinionIds = aliveMinions.map { it.uniqueId }

        summoner.setMinionIds(aliveMinionIds)
        return aliveMinions
    }

    fun addMinions(summoner: Entity, newMinions: Collection<Entity>) {
        val minionIds = newMinions.map { it.uniqueId }
            .toMutableList()
        val oldMinionsIds = summoner.getMinionIds()

        minionIds.addAll(oldMinionsIds)
        summoner.setMinionIds(minionIds)
    }

    fun setMinionsTarget(summoner: Creature, minions: Collection<LivingEntity>) {
        val target = summoner.target ?: return

        minions.mapNotNull { it as? Creature }
            .forEach { it.target = target }
    }

    private fun summonMinion(
        mapTier: Int,
        minionType: CustomEntityType,
        spawnLocation: Location,
    ): LivingEntity {
        val minion = EntityUtil.spawnCustomEntity(minionType, spawnLocation, mapTier) as LivingEntity

        minion.setIsEnemy()
        minion.setIsMinion()
        minion.setDisableDropEquipment()

        return minion
    }
}