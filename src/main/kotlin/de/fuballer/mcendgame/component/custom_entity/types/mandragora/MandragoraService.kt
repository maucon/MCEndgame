package de.fuballer.mcendgame.component.custom_entity.types.mandragora

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.SummonerUtil
import de.fuballer.mcendgame.util.extension.EntityExtension.getCustomEntityType
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent

@Component
class MandragoraService : Listener {
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    fun on(event: DamageCalculationEvent) {
        if (event.damager.getCustomEntityType() != MandragoraEntityType) return
        if (event.damager.type != EntityType.AREA_EFFECT_CLOUD) return

        event.cancel()
    }

    @EventHandler
    fun on(event: EntityDeathEvent) {
        val entity = event.entity
        if (entity.getCustomEntityType() != MandragoraEntityType) return

        for (vine in SummonerUtil.getMinionEntities(entity)) {
            vine.damage(100000.0)
        }
    }
}