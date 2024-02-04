package de.fuballer.mcendgame.technical.persistent_data.types

import de.fuballer.mcendgame.domain.attribute.AttributeType
import de.fuballer.mcendgame.domain.attribute.RolledAttribute
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

@Suppress("UNCHECKED_CAST")
object PersistentRolledAttributeList : PersistentDataType<PersistentDataContainer, List<RolledAttribute>> {
    override fun getPrimitiveType() = PersistentDataContainer::class.java

    override fun getComplexType() = List::class.java as Class<List<RolledAttribute>>

    override fun toPrimitive(complex: List<RolledAttribute>, context: PersistentDataAdapterContext): PersistentDataContainer {
        val container = context.newPersistentDataContainer()
        for (item in complex) {
            val key = PluginUtil.createNamespacedKey(item.type.toString())
            container.set(key, PersistentDataType.DOUBLE, item.roll)
        }
        return container
    }

    override fun fromPrimitive(primitive: PersistentDataContainer, context: PersistentDataAdapterContext) =
        primitive.keys.map {
            val type = AttributeType.valueOf(it.key.uppercase())
            val roll = primitive.get(it, PersistentDataType.DOUBLE)!!

            RolledAttribute(type, roll)
        }
}