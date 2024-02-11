package de.fuballer.mcendgame.technical.persistent_data.types.generic

import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

open class PersistentList<CLASS : Any, CLASS_CONVERTER : PersistentDataType<*, CLASS>>(
    private val tClass: CLASS_CONVERTER
) : PersistentDataType<PersistentDataContainer, List<CLASS>> {
    override fun getPrimitiveType() = PersistentDataContainer::class.java

    @Suppress("UNCHECKED_CAST")
    override fun getComplexType() = List::class.java as Class<List<CLASS>>

    override fun toPrimitive(complex: List<CLASS>, context: PersistentDataAdapterContext): PersistentDataContainer {
        val container = context.newPersistentDataContainer()

        for ((index, item) in complex.withIndex()) {
            val key = PluginUtil.createNamespacedKey(index.toString())
            container.set(key, tClass, item)
        }

        return container
    }

    override fun fromPrimitive(primitive: PersistentDataContainer, context: PersistentDataAdapterContext) =
        primitive.keys
            .sortedBy { it.key.toInt() }
            .map { primitive.get(it, tClass)!! }
}