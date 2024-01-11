package de.fuballer.mcendgame.component.custom_entity.persistent_data.types

import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import java.util.*

private const val DELIMITER = ","

@Suppress("UNCHECKED_CAST")
object PersistentUUIDList : PersistentDataType<String, List<UUID>> {
    override fun getPrimitiveType(): Class<String> = String::class.java

    override fun getComplexType(): Class<List<UUID>> = List::class.java as Class<List<UUID>>

    override fun toPrimitive(complex: List<UUID>, context: PersistentDataAdapterContext): String {
        return complex.joinToString(DELIMITER) { it.toString() }
    }

    override fun fromPrimitive(primitive: String, context: PersistentDataAdapterContext): List<UUID> {
        if (primitive.isEmpty()) return emptyList()

        return primitive.split(DELIMITER)
            .map { UUID.fromString(it) }
    }
}