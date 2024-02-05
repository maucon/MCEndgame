package de.fuballer.mcendgame.technical.persistent_data.types

import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import kotlin.reflect.KClass

class PersistentEnum<T : Enum<T>>(
    private val enumClass: KClass<T>
) : PersistentDataType<String, T> {
    override fun getPrimitiveType() = String::class.java

    override fun getComplexType() = enumClass.java

    override fun toPrimitive(complex: T, context: PersistentDataAdapterContext) = complex.name

    override fun fromPrimitive(primitive: String, context: PersistentDataAdapterContext): T = java.lang.Enum.valueOf(enumClass.java, primitive)
}