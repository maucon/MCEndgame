package de.fuballer.mcendgame.component.custom_entity.summoner

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.EntityExtension.getMinionIds
import de.fuballer.mcendgame.util.SummonerUtil
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.entity.Creature
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityTargetEvent

@Component
class SummonerTargetService : Listener {
    @EventHandler
    fun on(event: EntityTargetEvent) {
        val summoner = event.entity as? Creature ?: return
        val minionIds = summoner.getMinionIds() ?: return
        val minionEntities = WorldUtil.getFilteredEntities(summoner.world, minionIds, Creature::class)

        SummonerUtil.setMinionsTarget(summoner, minionEntities)
    }
}