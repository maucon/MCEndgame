package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.component.custom_entity.persistent_data.DataTypeKeys
import org.bukkit.entity.Entity

object PersistentDataUtil {
    fun <T> getValue(
        entity: Entity,
        typeKey: DataTypeKeys.TypeKey<T>
    ): T? = entity.persistentDataContainer.get(typeKey.key, typeKey.dataType)

    fun getBooleanValue(
        entity: Entity,
        typeKey: DataTypeKeys.TypeKey<Boolean>
    ): Boolean = entity.persistentDataContainer.get(typeKey.key, typeKey.dataType) ?: false

    fun <T : Any> setValue(
        entity: Entity,
        typeKey: DataTypeKeys.TypeKey<T>,
        value: T
    ) = entity.persistentDataContainer.set(typeKey.key, typeKey.dataType, value)
}