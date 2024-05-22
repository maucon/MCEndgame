package de.fuballer.mcendgame.component.custom_entity.summoner

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import de.fuballer.mcendgame.event.DungeonEnemySpawnedEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.LifeCycleListener
import de.fuballer.mcendgame.util.EntityUtil
import de.fuballer.mcendgame.util.SummonerUtil
import de.fuballer.mcendgame.util.extension.EntityExtension.getMapTier
import de.fuballer.mcendgame.util.extension.EntityExtension.setDisableDropEquipment
import de.fuballer.mcendgame.util.extension.EntityExtension.setIsMinion
import org.bukkit.Location
import org.bukkit.entity.Creature
import org.bukkit.entity.LivingEntity
import org.bukkit.plugin.java.JavaPlugin

@Component
class SummonerService : LifeCycleListener {

    override fun initialize(plugin: JavaPlugin) {
        SummonerUtil.summonerService = this
    }

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

        SummonerUtil.addMinions(summoner, minions)
        SummonerUtil.setMinionsTarget(summoner, minions)

        val event = DungeonEnemySpawnedEvent(summoner.world, minions)
        EventGateway.apply(event)

        return minions
    }

    private fun summonMinion(
        mapTier: Int,
        minionType: CustomEntityType,
        spawnLocation: Location,
    ): LivingEntity {
        val minion = EntityUtil.spawnCustomEntity(minionType, spawnLocation, mapTier) as LivingEntity

        minion.setIsMinion()
        minion.setDisableDropEquipment()

        return minion
    }
}