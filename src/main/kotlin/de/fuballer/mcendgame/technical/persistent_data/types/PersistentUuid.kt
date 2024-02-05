package de.fuballer.mcendgame.technical.persistent_data.types

import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import java.util.*

object PersistentUuid : PersistentDataType<String, UUID> {
    override fun getPrimitiveType() = String::class.java

    override fun getComplexType() = UUID::class.java

    override fun toPrimitive(complex: UUID, context: PersistentDataAdapterContext) = complex.toString()

    override fun fromPrimitive(primitive: String, context: PersistentDataAdapterContext): UUID = UUID.fromString(primitive)
}