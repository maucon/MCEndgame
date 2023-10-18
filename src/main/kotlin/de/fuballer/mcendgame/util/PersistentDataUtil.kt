package de.fuballer.mcendgame.util

import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

object PersistentDataUtil {
    fun <T> getValue(
        persistentDataContainer: PersistentDataContainer,
        key: NamespacedKey,
        dataType: PersistentDataType<*, T>
    ): T? {
        if (!persistentDataContainer.has(key, dataType)) return null
        return persistentDataContainer.get(key, dataType)!!
    }
}
