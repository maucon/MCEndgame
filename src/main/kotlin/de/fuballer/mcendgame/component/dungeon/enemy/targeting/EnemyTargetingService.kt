package de.fuballer.mcendgame.component.dungeon.enemy.targeting

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.event.entity.EntityTargetEvent.TargetReason

@Component
class EnemyTargetingService : Listener {
    @EventHandler
    fun onEntityTarget(event: EntityTargetEvent) {
        val entity = event.entity

        if (WorldUtil.isNotDungeonWorld(entity.world)) return
        if (isPlayerOrCompanion(entity)) return

        if (event.reason == TargetReason.TARGET_ATTACKED_NEARBY_ENTITY) {
            event.isCancelled = true
            return
        }

        val target = event.target ?: return
        if (isPlayerOrCompanion(target)) return

        event.isCancelled = true
    }

    private fun isPlayerOrCompanion(entity: Entity) = entity.type == EntityType.PLAYER || entity.type == EntityType.WOLF
}
