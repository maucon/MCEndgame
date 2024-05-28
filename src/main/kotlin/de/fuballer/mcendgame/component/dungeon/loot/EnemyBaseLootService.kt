package de.fuballer.mcendgame.component.dungeon.loot

import de.fuballer.mcendgame.event.DungeonEntityDeathEvent
import de.fuballer.mcendgame.framework.annotation.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@Component
class EnemyBaseLootService : Listener {
    @EventHandler
    fun on(event: DungeonEntityDeathEvent) {
        event.drops.clear()
    }
}