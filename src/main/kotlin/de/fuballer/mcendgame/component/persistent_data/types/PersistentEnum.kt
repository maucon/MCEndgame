package de.fuballer.mcendgame.component.persistent_data.types

import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import kotlin.reflect.KClass

class PersistentEnum<T : Enum<T>>(
    private val enumClass: KClass<T>
) : PersistentDataType<String, T> {
    override fun getPrimitiveType(): Class<String> = String::class.java

    override fun getComplexType(): Class<T> = enumClass.java

    override fun toPrimitive(complex: T, context: PersistentDataAdapterContext): String = complex.name

    override fun fromPrimitive(primitive: String, context: PersistentDataAdapterContext): T = java.lang.Enum.valueOf(enumClass.java, primitive)
}