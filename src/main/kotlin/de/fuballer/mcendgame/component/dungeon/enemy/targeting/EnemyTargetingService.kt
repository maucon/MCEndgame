package de.fuballer.mcendgame.component.dungeon.enemy.targeting

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.DungeonUtil
import de.fuballer.mcendgame.util.WorldUtil
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
        if (!DungeonUtil.isEnemyOrEnemyProjectile(entity)) return

        if (event.reason == TargetReason.TARGET_ATTACKED_NEARBY_ENTITY) {
            event.isCancelled = true
            return
        }

        val target = event.target ?: return
        if (!DungeonUtil.isEnemyOrEnemyProjectile(target)) return

        event.isCancelled = true
    }
}
