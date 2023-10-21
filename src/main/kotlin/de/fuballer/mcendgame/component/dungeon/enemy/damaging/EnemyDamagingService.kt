package de.fuballer.mcendgame.component.dungeon.enemy.damaging

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.DungeonUtil
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.event.entity.EntityTargetEvent.TargetReason

@Component
class EnemyDamagingService : Listener {
    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        val damager = event.damager

        if (WorldUtil.isNotDungeonWorld(damager.world)) return
        if (DungeonUtil.isPlayerOrCompanion(damager)) return

        val target = event.entity
        if (!DungeonUtil.isPlayerOrCompanion(target)) {
            event.isCancelled = true
            return
        }

        event.damage *= 2.0 / 3.0
    }
}
