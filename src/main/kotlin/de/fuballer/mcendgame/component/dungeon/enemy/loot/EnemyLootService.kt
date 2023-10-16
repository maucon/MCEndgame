package de.fuballer.mcendgame.component.dungeon.enemy.loot

import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.Keys
import de.fuballer.mcendgame.helper.PersistentDataUtil
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.persistence.PersistentDataType

class EnemyLootService {
    fun onEntityDeath(event: EntityDeathEvent) {
        if (PersistentDataUtil.getValue(event.entity.persistentDataContainer, Keys.DROP_BASE_LOOT, PersistentDataType.BOOLEAN) != false) return

        event.drops.clear()
    }
}