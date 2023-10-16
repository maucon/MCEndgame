package de.fuballer.mcendgame.component.dungeon.enemy.targeting

import de.fuballer.mcendgame.framework.stereotype.Service
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.entity.Monster
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.event.entity.EntityTargetEvent.TargetReason

class EnemyTargetingService : Service {
    fun onEntityTarget(event: EntityTargetEvent) {
        val entity = event.entity

        if (WorldUtil.isNotDungeonWorld(entity.world)) return
        if (entity !is Monster) return

        if (event.reason == TargetReason.TARGET_ATTACKED_NEARBY_ENTITY) {
            event.isCancelled = true
            return
        }

        if (event.target !is Monster) return

        event.isCancelled = true
    }
}
