package de.fuballer.mcendgame.component.dungeon.enemy.custom_entity

import de.fuballer.mcendgame.util.PluginUtil.createNamespacedKey
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataType

object DataTypeKeys {
    val DROP_BASE_LOOT = TypeKey(createNamespacedKey("drop-base-loot"), PersistentDataType.BOOLEAN)
    val DROP_EQUIPMENT = TypeKey(createNamespacedKey("drop-equipment"), PersistentDataType.BOOLEAN)
    val MAP_TIER = TypeKey(createNamespacedKey("map-tier"), PersistentDataType.INTEGER)
    val IS_MINION = TypeKey(createNamespacedKey("is-minion"), PersistentDataType.BOOLEAN)
    val HIDE_EQUIPMENT = TypeKey(createNamespacedKey("hide-equipment"), PersistentDataType.BOOLEAN)
    val ENTITY_TYPE = TypeKey(createNamespacedKey("entity-type"), PersistentDataType.STRING)

    class TypeKey<T>(
        val key: NamespacedKey,
        val dataType: PersistentDataType<*, T>
    )
}
