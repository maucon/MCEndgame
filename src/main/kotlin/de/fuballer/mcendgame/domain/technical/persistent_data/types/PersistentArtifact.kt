package de.fuballer.mcendgame.domain.technical.persistent_data.types

import de.fuballer.mcendgame.component.artifact.Artifact
import de.fuballer.mcendgame.domain.artifact.ArtifactType
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

object PersistentArtifact : PersistentDataType<PersistentDataContainer, Artifact> {
    override fun getPrimitiveType() = PersistentDataContainer::class.java

    override fun getComplexType() = Artifact::class.java

    override fun toPrimitive(complex: Artifact, context: PersistentDataAdapterContext): PersistentDataContainer {
        val container = context.newPersistentDataContainer()

        val key = PluginUtil.createNamespacedKey(complex.type.toString())
        container.set(key, PersistentDataType.INTEGER, complex.tier)

        return container
    }

    override fun fromPrimitive(primitive: PersistentDataContainer, context: PersistentDataAdapterContext): Artifact {
        val typeKey = primitive.keys.first()!!
        val type = ArtifactType.valueOf(typeKey.key.uppercase())
        val tier = primitive.get(typeKey, PersistentDataType.INTEGER)!!

        return Artifact(type, tier)
    }
}