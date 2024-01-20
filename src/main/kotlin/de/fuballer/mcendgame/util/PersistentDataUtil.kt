package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.domain.technical.persistent_data.TypeKeys
import org.bukkit.persistence.PersistentDataHolder

object PersistentDataUtil {
    fun <T> getValue(
        holder: PersistentDataHolder,
        typeKey: TypeKeys.TypeKey<T>
    ): T? = holder.persistentDataContainer.get(typeKey.key, typeKey.dataType)

    fun getBooleanValue(
        holder: PersistentDataHolder,
        typeKey: TypeKeys.TypeKey<Boolean>,
        default: Boolean = false
    ): Boolean = holder.persistentDataContainer.get(typeKey.key, typeKey.dataType) ?: default

    fun <T : Any> setValue(
        holder: PersistentDataHolder,
        typeKey: TypeKeys.TypeKey<T>,
        value: T
    ) = holder.persistentDataContainer.set(typeKey.key, typeKey.dataType, value)
}