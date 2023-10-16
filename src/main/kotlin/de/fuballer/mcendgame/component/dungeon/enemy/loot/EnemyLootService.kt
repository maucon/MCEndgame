package de.fuballer.mcendgame.component.dungeon.enemy.loot

import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.Keys
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.persistence.PersistentDataType

class EnemyLootService {
    fun onEntityDeath(event: EntityDeathEvent) {
        if (!event.entity.persistentDataContainer.has(Keys.DROP_BASE_LOOT, PersistentDataType.BOOLEAN)) return
        if (event.entity.persistentDataContainer.get(Keys.DROP_BASE_LOOT, PersistentDataType.BOOLEAN) ?: return) return

        event.drops.clear()
    }
}