package de.fuballer.mcendgame.domain.entity.mandragora

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.EntityUtil
import de.fuballer.mcendgame.util.SummonerUtil
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent

@Component
class MandragoraService : Listener {
    @EventHandler
    fun onMandragoraDeath(event: EntityDeathEvent) {
        val mandragora = event.entity

        if (!EntityUtil.isCustomEntityType(mandragora, MandragoraEntityType)) return

        for (vine in SummonerUtil.getMinionEntities(mandragora)) {
            vine.damage(100000.0)
        }
    }

    @EventHandler
    fun onMandragoraDamagedByCloud(event: EntityDamageByEntityEvent) {
        if (!EntityUtil.isCustomEntityType(event.entity, MandragoraEntityType)) return
        if (event.damager.type != EntityType.AREA_EFFECT_CLOUD) return
        event.isCancelled = true
    }
}