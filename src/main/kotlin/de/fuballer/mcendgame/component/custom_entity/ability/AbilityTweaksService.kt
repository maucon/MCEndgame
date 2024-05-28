package de.fuballer.mcendgame.component.custom_entity.ability

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.EntityExtension.isForcedVehicle
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.spigotmc.event.entity.EntityDismountEvent

@Component
class AbilityTweaksService : Listener {
    @EventHandler
    fun on(event: EntityDismountEvent) {
        val vehicle = event.dismounted
        if (!vehicle.isForcedVehicle()) return

        event.cancel()
    }
}