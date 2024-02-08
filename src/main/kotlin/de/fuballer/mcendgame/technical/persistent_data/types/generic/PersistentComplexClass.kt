package de.fuballer.mcendgame.technical.persistent_data.types.generic

import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

val MAP: Map<KClass<*>, PersistentDataType<*, *>> = mapOf(
//    ArtifactType::class to PersistentObjectClass(ArtifactType::class),
//    ArtifactTier::class to PersistentEnum(ArtifactTier::class),
)

@Deprecated("not working for now") // FIXME
open class PersistentComplexClass<T : Any>(
    private val classType: KClass<T>
) : PersistentDataType<PersistentDataContainer, T> {
    override fun getPrimitiveType() = PersistentDataContainer::class.java

    override fun getComplexType() = classType.java

    @Suppress("UNCHECKED_CAST")
    override fun toPrimitive(complex: T, context: PersistentDataAdapterContext): PersistentDataContainer {
        val container = context.newPersistentDataContainer()

        classType.memberProperties.forEach {
            val value = it.call(complex)

            if (value != null) {
                val key = PluginUtil.createNamespacedKey(it.name)
                val persistentDataType = MAP[it.returnType.jvmErasure]!! as PersistentDataType<Any, Any>
                container.set(key, persistentDataType, value)
            }
        }

        return container
    }

    override fun fromPrimitive(primitive: PersistentDataContainer, context: PersistentDataAdapterContext): T {
        val params = classType.memberProperties.map {
            val namespacedKey = PluginUtil.createNamespacedKey(it.name)
            primitive.get(namespacedKey, MAP[it.returnType.jvmErasure]!!)
        }

        return classType.primaryConstructor!!.call(*params.toTypedArray())
    }
}