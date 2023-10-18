package de.fuballer.mcendgame.component.dungeon.looting

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.EventListener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDeathEvent

@Component
class LootingListener(
    private val lootingService: LootingService
) : EventListener {
    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) = lootingService.onEntityDeath(event)
}
