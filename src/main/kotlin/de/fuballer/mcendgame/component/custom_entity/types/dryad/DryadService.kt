package de.fuballer.mcendgame.component.custom_entity.types.dryad

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.EntityUtil
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

@Component
class DryadService : Listener {
    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (!EntityUtil.isCustomEntityType(event.damager, DryadEntityType)) return

        val damagedEntity = event.entity as? LivingEntity ?: return

        damagedEntity.addPotionEffect(DryadSettings.POISON_EFFECT)
    }
}
