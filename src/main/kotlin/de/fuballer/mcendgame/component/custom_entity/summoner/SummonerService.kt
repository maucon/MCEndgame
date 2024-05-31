package de.fuballer.mcendgame.component.custom_entity.summoner

import de.fuballer.mcendgame.event.DungeonEntityDeathEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.WorldUtil
import de.fuballer.mcendgame.util.extension.EntityExtension.getMinionIds
import org.bukkit.entity.Creature
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityTargetEvent

@Component
class SummonerService : Listener {
    @EventHandler
    fun on(event: DungeonEntityDeathEvent) {
        SummonerUtil.getMinionEntities(event.entity)
            .forEach { it.remove() }
    }

    @EventHandler
    fun on(event: EntityTargetEvent) {
        val summoner = event.entity as? Creature ?: return
        val minionIds = summoner.getMinionIds()
        val minionEntities = WorldUtil.getFilteredEntities(summoner.world, minionIds, Creature::class)

        SummonerUtil.setMinionsTarget(summoner, minionEntities)
    }
}