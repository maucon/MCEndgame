package de.fuballer.mcendgame.component.dungeon.enemy

import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.DungeonUtil
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.event.entity.EntityTargetEvent.TargetReason

@Service
class EnemyTargetingService : Listener {
    @EventHandler
    fun on(event: EntityTargetEvent) {
        val entity = event.entity

        if (!entity.world.isDungeonWorld()) return
        if (!DungeonUtil.isEnemyOrEnemyProjectile(entity)) return

        if (event.reason == TargetReason.TARGET_ATTACKED_NEARBY_ENTITY) {
            event.cancel()
            return
        }

        val target = event.target ?: return
        if (DungeonUtil.isEnemyOrEnemyProjectile(target)) {
            event.cancel()
        }
    }
}