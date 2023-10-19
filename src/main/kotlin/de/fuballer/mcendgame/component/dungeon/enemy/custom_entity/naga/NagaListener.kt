package de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.naga

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.EventListener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityShootBowEvent

@Component
class NagaListener(
    private val nagaService: NagaService
) : EventListener {
    @EventHandler
    fun onEntityShootBow(event: EntityShootBowEvent) = nagaService.onEntityShootBow(event)

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) = nagaService.onEntityDamageByEntity(event)
}