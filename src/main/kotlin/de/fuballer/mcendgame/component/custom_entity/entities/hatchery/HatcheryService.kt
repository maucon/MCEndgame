package de.fuballer.mcendgame.component.custom_entity.entities.hatchery

import de.fuballer.mcendgame.component.custom_entity.data.CustomEntityType
import de.fuballer.mcendgame.component.custom_entity.summoner.SummonerService
import de.fuballer.mcendgame.component.custom_entity.summoner.db.MinionRepository
import de.fuballer.mcendgame.component.custom_entity.summoner.db.MinionsEntity
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.entity.Creature
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.util.Vector
import kotlin.random.Random

@Component
class HatcheryService(
    private val minionRepo: MinionRepository,
    private val summonerService: SummonerService
) : Listener {
    @EventHandler
    fun onEntityShootBow(event: EntityShootBowEvent) {
        if (!CustomEntityType.isType(event.entity, CustomEntityType.HATCHERY)) return

        event.isCancelled = true
        summonLeech(event)
    }

    private fun summonLeech(event: EntityShootBowEvent) {
        if (Random.nextDouble() > HatcherySettings.MINION_SPAWN_PROBABILITY) return

        val hatchery = event.entity as Creature
        val minionsEntity = minionRepo.findById(hatchery.uniqueId)
            ?: MinionsEntity(event.entity.uniqueId)

        updateMinions(minionsEntity)

        if (minionsEntity.minions.size >= HatcherySettings.MAX_MINIONS) return

        summonerService.summonMinions(
            hatchery,
            CustomEntityType.LEECH,
            amount = 1, weapons = true, ranged = false, armor = true,
            HatcherySettings.MINION_HEALTH,
            Vector(0, 1, 0)
        )
    }

    private fun updateMinions(minionsEntity: MinionsEntity) {
        minionsEntity.minions.removeIf { it.isDead }
    }
}