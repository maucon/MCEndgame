package de.fuballer.mcendgame.technical.persistent_data.types

import de.fuballer.mcendgame.component.totem.data.Totem
import de.fuballer.mcendgame.component.totem.data.TotemTier
import de.fuballer.mcendgame.component.totem.data.TotemType
import de.fuballer.mcendgame.technical.persistent_data.types.generic.PersistentEnum
import de.fuballer.mcendgame.technical.persistent_data.types.generic.PersistentObjectClass
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

private val TYPE_KEY = PluginUtil.createNamespacedKey("type")
private val TIER_KEY = PluginUtil.createNamespacedKey("tier")

object PersistentTotem : PersistentDataType<PersistentDataContainer, Totem> {
    override fun getPrimitiveType() = PersistentDataContainer::class.java

    override fun getComplexType() = Totem::class.java

    override fun toPrimitive(complex: Totem, context: PersistentDataAdapterContext): PersistentDataContainer {
        val container = context.newPersistentDataContainer()

        container.set(TYPE_KEY, PersistentObjectClass(TotemType::class), complex.type)
        container.set(TIER_KEY, PersistentEnum(TotemTier::class), complex.tier)

        return container
    }

    override fun fromPrimitive(primitive: PersistentDataContainer, context: PersistentDataAdapterContext): Totem {
        val type = primitive.get(TYPE_KEY, PersistentObjectClass(TotemType::class))!!
        val tier = primitive.get(TIER_KEY, PersistentEnum(TotemTier::class))!!

        return Totem(type, tier)
    }
}