package de.fuballer.mcendgame.technical.persistent_data.types

import de.fuballer.mcendgame.component.artifact.data.Artifact
import de.fuballer.mcendgame.component.artifact.data.ArtifactTier
import de.fuballer.mcendgame.component.artifact.data.ArtifactType
import de.fuballer.mcendgame.technical.persistent_data.types.generic.PersistentEnum
import de.fuballer.mcendgame.technical.persistent_data.types.generic.PersistentObjectClass
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

private val TYPE_KEY = PluginUtil.createNamespacedKey("type")
private val TIER_KEY = PluginUtil.createNamespacedKey("tier")

object PersistentArtifact : PersistentDataType<PersistentDataContainer, Artifact> {
    override fun getPrimitiveType() = PersistentDataContainer::class.java

    override fun getComplexType() = Artifact::class.java

    override fun toPrimitive(complex: Artifact, context: PersistentDataAdapterContext): PersistentDataContainer {
        val container = context.newPersistentDataContainer()

        container.set(TYPE_KEY, PersistentObjectClass(ArtifactType::class), complex.type)
        container.set(TIER_KEY, PersistentEnum(ArtifactTier::class), complex.tier)

        return container
    }

    override fun fromPrimitive(primitive: PersistentDataContainer, context: PersistentDataAdapterContext): Artifact {
        val type = primitive.get(TYPE_KEY, PersistentObjectClass(ArtifactType::class))!!
        val tier = primitive.get(TIER_KEY, PersistentEnum(ArtifactTier::class))!!

        return Artifact(type, tier)
    }
}