package de.fuballer.mcendgame.component.persistent_data

import de.fuballer.mcendgame.component.persistent_data.types.PersistentCustomEntityType
import de.fuballer.mcendgame.component.persistent_data.types.PersistentRolledAttributeList
import de.fuballer.mcendgame.component.persistent_data.types.PersistentUuidSet
import de.fuballer.mcendgame.util.PluginUtil.createNamespacedKey
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataType

object DataTypeKeys {
    // entities
    val DROP_EQUIPMENT = TypeKey(createNamespacedKey("drop-equipment"), PersistentDataType.BOOLEAN)
    val MAP_TIER = TypeKey(createNamespacedKey("map-tier"), PersistentDataType.INTEGER)
    val IS_MINION = TypeKey(createNamespacedKey("is-minion"), PersistentDataType.BOOLEAN)
    val HIDE_EQUIPMENT = TypeKey(createNamespacedKey("hide-equipment"), PersistentDataType.BOOLEAN)
    val CUSTOM_ENTITY_TYPE = TypeKey(createNamespacedKey("custom-entity-type"), PersistentCustomEntityType)
    val IS_ENEMY = TypeKey(createNamespacedKey("is-enemy"), PersistentDataType.BOOLEAN)
    val MINIONS = TypeKey(createNamespacedKey("minions"), PersistentUuidSet)

    // items
    val ATTRIBUTES = TypeKey(createNamespacedKey("attributes"), PersistentRolledAttributeList)
    val CORRUPTED = TypeKey(createNamespacedKey("corrupted"), PersistentDataType.BOOLEAN)

    class TypeKey<T>(
        val key: NamespacedKey,
        val dataType: PersistentDataType<*, T>
    )
}
