package de.fuballer.mcendgame.component.custom_entity.types.wendigo

import de.fuballer.mcendgame.component.technical.extension.EntityExtension.getCustomEntityType
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

@Component
class WendigoService : Listener {
    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        if (event.damager.getCustomEntityType() != WendigoEntityType) return

        val damagedEntity = event.entity as? LivingEntity ?: return
        damagedEntity.addPotionEffect(WendigoSettings.DARKNESS_EFFECT)
    }
}
