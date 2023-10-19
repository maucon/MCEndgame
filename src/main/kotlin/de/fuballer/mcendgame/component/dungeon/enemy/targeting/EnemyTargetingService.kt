package de.fuballer.mcendgame.component.dungeon.enemy.targeting

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.event.entity.EntityTargetEvent.TargetReason

@Component
class EnemyTargetingService {
    fun onEntityTarget(event: EntityTargetEvent) {
        val entity = event.entity

        if (WorldUtil.isNotDungeonWorld(entity.world)) return
        if (isPlayerOrPlayerMinion(entity)) return

        if (event.reason == TargetReason.TARGET_ATTACKED_NEARBY_ENTITY) {
            event.isCancelled = true
            return
        }

        val target = event.target ?: return
        if (isPlayerOrPlayerMinion(target)) return

        event.isCancelled = true
    }

    private fun isPlayerOrPlayerMinion(entity: Entity) = entity.type == EntityType.PLAYER || entity.type == EntityType.WOLF
}
