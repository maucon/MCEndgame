package de.fuballer.mcendgame.component.custom_entity.summoner

import de.fuballer.mcendgame.component.dungeon.enemy.equipment.EquipmentGenerationService
import de.fuballer.mcendgame.component.dungeon.enemy.generation.EnemyGenerationService
import de.fuballer.mcendgame.domain.entity.CustomEntityType
import de.fuballer.mcendgame.domain.technical.persistent_data.TypeKeys
import de.fuballer.mcendgame.event.DungeonEnemySpawnedEvent
import de.fuballer.mcendgame.event.EventGateway
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.EntityUtil
import de.fuballer.mcendgame.util.PersistentDataUtil
import de.fuballer.mcendgame.util.SummonerUtil
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Creature
import org.bukkit.entity.LivingEntity
import org.bukkit.util.Vector
import kotlin.random.Random

@Component
class SummonerService(
    private val equipmentGenerationService: EquipmentGenerationService,
    private val enemyGenerationService: EnemyGenerationService
) {
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
        val mapTier = PersistentDataUtil.getValue(summoner, TypeKeys.MAP_TIER) ?: -1

        val minions = (0 until amount)
            .map { summonMinion(Random, summoner, mapTier, minionType, weapons, ranged, armor, health, spawnOffset) }
            .toSet()

        SummonerUtil.addMinions(summoner, minions)
        SummonerUtil.setMinionsTarget(summoner, minions)

        val event = DungeonEnemySpawnedEvent(summoner.world, minions)
        EventGateway.apply(event)
    }

    private fun summonMinion(
        random: Random,
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

        EntityUtil.setAttribute(minion, Attribute.GENERIC_MAX_HEALTH, health)
        minion.health = health

        PersistentDataUtil.setValue(minion, TypeKeys.IS_MINION, true)
        PersistentDataUtil.setValue(minion, TypeKeys.DISABLE_DROP_EQUIPMENT, true)

        if (mapTier < 0 || minion !is Creature) return minion

        equipmentGenerationService.setCreatureEquipment(random, minion, mapTier, weapons, ranged, armor)

        val canBeInvisible = !minionType.hideEquipment
        enemyGenerationService.addEffectsToEntity(random, minion, mapTier, canBeInvisible)

        return minion
    }
}