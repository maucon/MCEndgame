package de.fuballer.mcendgame.component.custom_entity.summoner

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import de.fuballer.mcendgame.component.dungeon.enemy.equipment.EquipmentGenerationService
import de.fuballer.mcendgame.component.dungeon.enemy.generation.EnemyGenerationService
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
import kotlin.random.Random

@Component
class SummonerService(
    private val equipmentGenerationService: EquipmentGenerationService,
    private val enemyGenerationService: EnemyGenerationService
) : LifeCycleListener {

    override fun initialize(plugin: JavaPlugin) {
        super.initialize(plugin)
        SummonerUtil.summonerService = this
    }

    fun summonMinions(
        summoner: Creature,
        minionType: CustomEntityType,
        amount: Int,
        weapons: Boolean,
        ranged: Boolean,
        armor: Boolean,
        spawnLocation: Location,
    ): Set<LivingEntity> {
        val mapTier = summoner.getMapTier() ?: -1

        val minions = (0 until amount)
            .map { summonMinion(Random, mapTier, minionType, weapons, ranged, armor, spawnLocation) }
            .toSet()

        SummonerUtil.addMinions(summoner, minions)
        SummonerUtil.setMinionsTarget(summoner, minions)

        val event = DungeonEnemySpawnedEvent(summoner.world, minions)
        EventGateway.apply(event)

        return minions
    }

    private fun summonMinion(
        random: Random,
        mapTier: Int,
        minionType: CustomEntityType,
        weapons: Boolean,
        ranged: Boolean,
        armor: Boolean,
        spawnLocation: Location,
    ): LivingEntity {
        val minion = EntityUtil.spawnCustomEntity(minionType, spawnLocation, mapTier) as LivingEntity

        minion.setIsMinion()
        minion.setDisableDropEquipment()

        if (mapTier < 0 || minion !is Creature) return minion

        equipmentGenerationService.generate(random, minion, mapTier, weapons, ranged, armor)

        val canBeInvisible = !minionType.hideEquipment
        enemyGenerationService.addEffectsToEnemy(random, minion, mapTier, canBeInvisible)

        return minion
    }
}