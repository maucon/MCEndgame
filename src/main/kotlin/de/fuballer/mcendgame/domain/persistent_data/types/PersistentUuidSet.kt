package de.fuballer.mcendgame.domain.persistent_data.types

import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import java.util.*

private const val DELIMITER = ","

@Suppress("UNCHECKED_CAST")
object PersistentUuidSet : PersistentDataType<String, Set<UUID>> {
    override fun getPrimitiveType(): Class<String> = String::class.java

    override fun getComplexType(): Class<Set<UUID>> = Set::class.java as Class<Set<UUID>>

    override fun toPrimitive(complex: Set<UUID>, context: PersistentDataAdapterContext): String {
        return complex.joinToString(DELIMITER) { it.toString() }
    }

    override fun fromPrimitive(primitive: String, context: PersistentDataAdapterContext): Set<UUID> {
        if (primitive.isEmpty()) return emptySet()

        return primitive.split(DELIMITER)
            .map { UUID.fromString(it) }
            .toSet()
    }
}