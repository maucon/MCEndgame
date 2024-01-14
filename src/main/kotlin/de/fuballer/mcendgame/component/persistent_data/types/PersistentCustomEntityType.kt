package de.fuballer.mcendgame.component.persistent_data.types

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType

object PersistentCustomEntityType : PersistentDataType<String, CustomEntityType> {
    override fun getPrimitiveType(): Class<String> = String::class.java

    override fun getComplexType(): Class<CustomEntityType> = CustomEntityType::class.java

    override fun fromPrimitive(primitive: String, context: PersistentDataAdapterContext): CustomEntityType {
        val kClass = Class.forName(primitive).kotlin
        return kClass.objectInstance as? CustomEntityType
            ?: throw IllegalArgumentException("No CustomEntityType found for name: $primitive")
    }

    override fun toPrimitive(complex: CustomEntityType, context: PersistentDataAdapterContext): String = complex::class.qualifiedName!!
}