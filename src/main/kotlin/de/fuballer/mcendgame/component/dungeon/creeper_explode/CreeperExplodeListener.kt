package de.fuballer.mcendgame.component.dungeon.creeper_explode

import de.fuballer.mcendgame.framework.stereotype.EventListener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.ExplosionPrimeEvent

class CreeperExplodeListener(
    private val creeperExplodeService: CreeperExplodeService
) : EventListener {
    @EventHandler
    fun onEntityPrimed(event: ExplosionPrimeEvent) = creeperExplodeService.onEntityPrimed(event)
}
