package de.fuballer.mcendgame.component.custom_entity.types.dryad

import de.fuballer.mcendgame.component.technical.extension.EntityExtension.getCustomEntityType
import de.fuballer.mcendgame.framework.annotation.Component
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
