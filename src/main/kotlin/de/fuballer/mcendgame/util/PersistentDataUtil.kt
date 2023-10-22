package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.data.DataTypeKeys
import org.bukkit.entity.Entity

object PersistentDataUtil {
    fun <T> getValue(
        entity: Entity,
        typeKey: DataTypeKeys.TypeKey<T>
    ): T? = entity.persistentDataContainer.get(typeKey.key, typeKey.dataType)

    fun <T> setValue(
        entity: Entity,
        typeKey: DataTypeKeys.TypeKey<T>,
        value: T & Any
    ) = entity.persistentDataContainer.set(typeKey.key, typeKey.dataType, value)
}