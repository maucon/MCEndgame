package de.fuballer.mcendgame.domain.persistent_data.types

import de.fuballer.mcendgame.domain.entity.CustomEntityType
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType

object PersistentCustomEntityType : PersistentDataType<String, CustomEntityType> {
    override fun getPrimitiveType() = String::class.java

    override fun getComplexType() = CustomEntityType::class.java

    override fun fromPrimitive(primitive: String, context: PersistentDataAdapterContext): CustomEntityType {
        val kClass = Class.forName(primitive).kotlin
        return kClass.objectInstance as? CustomEntityType
            ?: throw IllegalArgumentException("No CustomEntityType found for name: $primitive")
    }

    override fun toPrimitive(complex: CustomEntityType, context: PersistentDataAdapterContext) = complex::class.qualifiedName!!
}