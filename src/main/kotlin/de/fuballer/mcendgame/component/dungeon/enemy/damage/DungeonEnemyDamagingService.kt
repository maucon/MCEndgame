package de.fuballer.mcendgame.component.dungeon.enemy.damage

import de.fuballer.mcendgame.component.custom_entity.DataTypeKeys
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.DungeonUtil
import de.fuballer.mcendgame.util.PersistentDataUtil
import de.fuballer.mcendgame.util.WorldUtil
import org.bukkit.entity.EvokerFangs
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntitySpawnEvent

@Component
class DungeonEnemyDamagingService : Listener {
    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        val damager = event.damager

        if (WorldUtil.isNotDungeonWorld(damager.world)) return
        if (!DungeonUtil.isEnemyOrEnemyProjectile(damager)) return

        val target = event.entity
        if (DungeonUtil.isEnemyOrEnemyProjectile(target)) {
            event.isCancelled = true
            return
        }

        event.damage *= 2.0 / 3.0 // worlds on hard mode multiply damage by 1,5x, so we revert that
    }

    @EventHandler
    fun onEntitySpawn(event: EntitySpawnEvent) {
        val entity = event.entity
        if (entity !is EvokerFangs) return

        val owner = entity.owner ?: return
        if (PersistentDataUtil.getValue(owner, DataTypeKeys.IS_ENEMY) != true) return

        PersistentDataUtil.setValue(entity, DataTypeKeys.IS_ENEMY, true)
    }
}
