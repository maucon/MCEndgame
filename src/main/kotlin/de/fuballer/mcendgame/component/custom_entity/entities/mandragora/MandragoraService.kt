package de.fuballer.mcendgame.component.custom_entity.entities.mandragora

import de.fuballer.mcendgame.component.custom_entity.CustomEntityType
import de.fuballer.mcendgame.component.custom_entity.summoner.db.MinionRepository
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDeathEvent

@Component
class MandragoraService(
    private val minionRepo: MinionRepository
) : Listener {
    @EventHandler
    fun onMandragoraDeath(event: EntityDeathEvent) {
        val mandragora = event.entity

        if (!CustomEntityType.isType(mandragora, CustomEntityType.MANDRAGORA)) return
        if (!minionRepo.exists(mandragora.uniqueId)) return

        for (vine in minionRepo.getById(mandragora.uniqueId).minions) {
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