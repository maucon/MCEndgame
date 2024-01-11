package de.fuballer.mcendgame.component.dungeon.enemy.base_loot

import de.fuballer.mcendgame.component.custom_entity.persistent_data.DataTypeKeys
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.PersistentDataUtil
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent

@Component
class EnemyBaseLootService : Listener {
    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        if (PersistentDataUtil.getBooleanValue(event.entity, DataTypeKeys.DROP_BASE_LOOT)) return

        event.drops.clear()
    }
}
