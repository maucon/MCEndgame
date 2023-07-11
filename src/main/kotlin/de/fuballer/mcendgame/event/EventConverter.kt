package de.fuballer.mcendgame.event

import de.fuballer.mcendgame.framework.stereotype.EventListener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDeathEvent

class EventConverter : EventListener {
    @EventHandler
    fun onEntityDeath(initialEvent: EntityDeathEvent) {
//        if (WorldHelper.isNotDungeonWorld(initialEvent.entity.world)) return
//
//        val event = DungeonEntityDeathEvent(initialEvent)
//        Bukkit.getServer().pluginManager.callEvent(event)
    }
}
