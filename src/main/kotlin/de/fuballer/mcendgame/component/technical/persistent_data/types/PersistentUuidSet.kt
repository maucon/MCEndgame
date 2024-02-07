package de.fuballer.mcendgame.component.technical.persistent_data.types

import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import java.util.*

@Suppress("UNCHECKED_CAST")
object PersistentUuidSet : PersistentDataType<PersistentDataContainer, Set<UUID>> {
    override fun getPrimitiveType() = PersistentDataContainer::class.java

    override fun getComplexType() = Set::class.java as Class<Set<UUID>>

    override fun toPrimitive(complex: Set<UUID>, context: PersistentDataAdapterContext): PersistentDataContainer {
        val container = context.newPersistentDataContainer()
        for ((index, item) in complex.withIndex()) {
            val key = PluginUtil.createNamespacedKey(index.toString())
            container.set(key, PersistentDataType.STRING, item.toString())
        }
        return container
    }

    override fun fromPrimitive(primitive: PersistentDataContainer, context: PersistentDataAdapterContext) =
        primitive.keys.map {
            val uuidString = primitive.get(it, PersistentDataType.STRING)!!
            UUID.fromString(uuidString)
        }.toSet()
}