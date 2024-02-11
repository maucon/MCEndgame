package de.fuballer.mcendgame.technical.persistent_data.types.generic

import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
class PersistentObjectClass<T : Any>(
    private val typeClass: KClass<T>
) : PersistentDataType<String, T> {
    override fun getPrimitiveType() = String::class.java

    override fun getComplexType() = typeClass.java

    override fun fromPrimitive(primitive: String, context: PersistentDataAdapterContext): T {
        val kClass = Class.forName(primitive).kotlin
        return kClass.objectInstance as? T
            ?: throw IllegalArgumentException("No object class found for name: $primitive")
    }

    override fun toPrimitive(complex: T, context: PersistentDataAdapterContext) = complex::class.qualifiedName!!
}