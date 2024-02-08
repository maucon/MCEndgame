package de.fuballer.mcendgame.component.custom_entity.types.hatchery

import de.fuballer.mcendgame.component.custom_entity.summoner.SummonerService
import de.fuballer.mcendgame.component.custom_entity.types.leech.LeechEntityType
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.EntityExtension.getCustomEntityType
import de.fuballer.mcendgame.util.SummonerUtil
import org.bukkit.entity.Bee
import org.bukkit.entity.Creature
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityShootBowEvent
import org.bukkit.util.Vector
import kotlin.random.Random

@Component
class HatcheryService(
    private val summonerService: SummonerService
) : Listener {
    @EventHandler
    fun onEntityShootBow(event: EntityShootBowEvent) {
        if (event.entity.getCustomEntityType() != HatcheryEntityType) return

        event.isCancelled = true
        summonLeech(event)
    }

    private fun summonLeech(event: EntityShootBowEvent) {
        if (Random.nextDouble() > HatcherySettings.MINION_SPAWN_PROBABILITY) return

        val hatchery = event.entity as Creature
        val minions = SummonerUtil.getMinionEntities(hatchery)

        if (minions.size >= HatcherySettings.MAX_MINIONS) return

        summonerService.summonMinions(
            hatchery,
            LeechEntityType,
            amount = 1, weapons = true, ranged = false, armor = true,
            HatcherySettings.MINION_HEALTH,
            Vector(0, 1, 0)
        )

        setLeechAnger(minions)
    }

    private fun setLeechAnger(minions: List<Creature>) {
        minions.filterIsInstance<Bee>()
            .forEach { it.anger = Int.MAX_VALUE }
    }
}