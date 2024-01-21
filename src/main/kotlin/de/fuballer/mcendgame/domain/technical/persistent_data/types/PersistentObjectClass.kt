package de.fuballer.mcendgame.domain.technical.persistent_data.types

import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType

@Suppress("UNCHECKED_CAST")
class PersistentObjectClass<T : Any>(
    private val typeClass: Class<T>
) : PersistentDataType<String, T> {
    override fun getPrimitiveType() = String::class.java

    override fun getComplexType() = typeClass

    override fun fromPrimitive(primitive: String, context: PersistentDataAdapterContext): T {
        val kClass = Class.forName(primitive).kotlin
        return kClass.objectInstance as? T
            ?: throw IllegalArgumentException("No CustomEntityType found for name: $primitive")
    }

    override fun toPrimitive(complex: T, context: PersistentDataAdapterContext) = complex::class.qualifiedName!!
}