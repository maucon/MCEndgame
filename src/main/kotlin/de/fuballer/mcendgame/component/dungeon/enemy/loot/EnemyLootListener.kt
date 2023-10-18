package de.fuballer.mcendgame.component.dungeon.enemy.loot

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.EventListener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDeathEvent

@Component
class EnemyLootListener(
    private val enemyLootService: EnemyLootService
) : EventListener {
    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) = enemyLootService.onEntityDeath(event)
}