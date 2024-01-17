package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.domain.persistent_data.DataTypeKeys
import org.bukkit.persistence.PersistentDataHolder

object PersistentDataUtil {
    fun <T> getValue(
        holder: PersistentDataHolder,
        typeKey: DataTypeKeys.TypeKey<T>
    ): T? = holder.persistentDataContainer.get(typeKey.key, typeKey.dataType)

    fun getBooleanValue(
        holder: PersistentDataHolder,
        typeKey: DataTypeKeys.TypeKey<Boolean>,
        default: Boolean = false
    ): Boolean = holder.persistentDataContainer.get(typeKey.key, typeKey.dataType) ?: default

    fun <T : Any> setValue(
        holder: PersistentDataHolder,
        typeKey: DataTypeKeys.TypeKey<T>,
        value: T
    ) = holder.persistentDataContainer.set(typeKey.key, typeKey.dataType, value)
}