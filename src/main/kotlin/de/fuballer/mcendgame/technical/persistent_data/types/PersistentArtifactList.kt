package de.fuballer.mcendgame.technical.persistent_data.types

import de.fuballer.mcendgame.component.artifact.Artifact
import de.fuballer.mcendgame.domain.ArtifactType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

@Suppress("UNCHECKED_CAST")
object PersistentArtifactList : PersistentDataType<PersistentDataContainer, List<Artifact>> {
    override fun getPrimitiveType() = PersistentDataContainer::class.java

    override fun getComplexType() = List::class.java as Class<List<Artifact>>

    override fun toPrimitive(complex: List<Artifact>, context: PersistentDataAdapterContext): PersistentDataContainer {
        val container = context.newPersistentDataContainer()
        for ((index, item) in complex.withIndex()) {
            val artifactContainer = context.newPersistentDataContainer()
            val artifactTypeKey = PluginUtil.createNamespacedKey(item.type.toString())
            artifactContainer.set(artifactTypeKey, PersistentDataType.INTEGER, item.tier)

            val key = PluginUtil.createNamespacedKey(index.toString())
            container.set(key, PersistentDataType.TAG_CONTAINER, artifactContainer)
        }
        return container
    }

    override fun fromPrimitive(primitive: PersistentDataContainer, context: PersistentDataAdapterContext) =
        primitive.keys.map {
            val container = primitive.get(it, PersistentDataType.TAG_CONTAINER)!!

            val artifactTypeKey = container.keys.first()!!
            val type = ArtifactType.valueOf(artifactTypeKey.key.uppercase())
            val tier = container.get(artifactTypeKey, PersistentDataType.INTEGER)!!

            Artifact(type, tier)
        }
}