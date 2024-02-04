package de.fuballer.mcendgame.component.custom_entity.summoner

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.persistent_data.TypeKeys
import de.fuballer.mcendgame.util.PersistentDataUtil
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
        val minionIds = PersistentDataUtil.getValue(summoner, TypeKeys.MINION_IDS) ?: return
        val minionEntities = WorldUtil.getFilteredEntities(summoner.world, minionIds, Creature::class)

        SummonerUtil.setMinionsTarget(summoner, minionEntities)
    }
}