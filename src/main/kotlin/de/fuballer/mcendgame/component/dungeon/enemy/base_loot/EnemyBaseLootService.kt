package de.fuballer.mcendgame.component.dungeon.enemy.base_loot

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent

@Component
class EnemyBaseLootService : Listener {
    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        if (WorldUtil.isNotDungeonWorld(event.entity.world)) return

        event.drops.clear()
    }
}
