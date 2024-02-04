package de.fuballer.mcendgame.domain.entity.dryad

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.technical.extension.EntityExtension.getCustomEntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

@Component
class DryadService : Listener {
    @EventHandler
    fun on(event: EntityDamageByEntityEvent) {
        if (event.damager.getCustomEntityType() != DryadEntityType) return

        val damagedEntity = event.entity as? LivingEntity ?: return
        damagedEntity.addPotionEffect(DryadSettings.POISON_EFFECT)
    }
}
