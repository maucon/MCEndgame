package de.fuballer.mcendgame.component.dungeon.enemy.targeting

import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.framework.stereotype.EventListener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityTargetEvent

@Service
class EnemyTargetingListener(
    private val enemyTargetingService: EnemyTargetingService
) : EventListener {
    @EventHandler
    fun onEntityTarget(event: EntityTargetEvent) = enemyTargetingService.onEntityTarget(event)
}
