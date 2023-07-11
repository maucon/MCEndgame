package de.fuballer.mcendgame.component.dungeon.enemy

import de.fuballer.mcendgame.framework.stereotype.EventListener
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityPotionEffectEvent

class EnemyGenerationListener(
    private val enemyGenerationService: EnemyGenerationService
) : EventListener {
    @EventHandler
    fun onEntityPotionEffect(event: EntityPotionEffectEvent) = enemyGenerationService.onEntityPotionEffect(event)
}