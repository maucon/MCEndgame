package de.fuballer.mcendgame.component.dungeon.boss

import de.fuballer.mcendgame.framework.stereotype.EventListener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityTargetEvent

class DungeonBossListener(
    private val dungeonBossService: DungeonBossService
) : EventListener {
    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) = dungeonBossService.onEntityDeath(event)

    @EventHandler
    fun onEntityTarget(event: EntityTargetEvent) = dungeonBossService.onEntityTarget(event)

    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) = dungeonBossService.onEntityDamage(event)
}
