package de.fuballer.mcendgame.component.custom_entity.entities.mandragora

import de.fuballer.mcendgame.component.custom_entity.CustomEntityType
import de.fuballer.mcendgame.framework.annotation.Component
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

        if (!CustomEntityType.isType(mandragora, CustomEntityType.MANDRAGORA)) return

        for (vine in SummonerUtil.getMinionEntities(mandragora)) {
            vine.damage(100000.0)
        }
    }

    @EventHandler
    fun onMandragoraDamagedByCloud(event: EntityDamageByEntityEvent) {
        if (!CustomEntityType.isType(event.entity, CustomEntityType.MANDRAGORA)) return
        if (event.damager.type != EntityType.AREA_EFFECT_CLOUD) return
        event.isCancelled = true
    }
}