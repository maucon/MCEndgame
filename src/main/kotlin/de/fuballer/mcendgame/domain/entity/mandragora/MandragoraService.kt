package de.fuballer.mcendgame.domain.entity.mandragora

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.EntityExtension.getCustomEntityType
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
        val entity = event.entity
        if (entity.getCustomEntityType() != MandragoraEntityType) return

        for (vine in SummonerUtil.getMinionEntities(entity)) {
            vine.damage(100000.0)
        }
    }

    @EventHandler
    fun onMandragoraDamagedByCloud(event: EntityDamageByEntityEvent) {
        if (event.entity.getCustomEntityType() != MandragoraEntityType) return
        if (event.damager.type != EntityType.AREA_EFFECT_CLOUD) return

        event.isCancelled = true
    }
}