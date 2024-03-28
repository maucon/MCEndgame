package de.fuballer.mcendgame.technical.persistent_data.types

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.component.item.attribute.RolledAttribute
import de.fuballer.mcendgame.technical.persistent_data.types.generic.PersistentEnum
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

private val TYPE_KEY = PluginUtil.createNamespacedKey("type")
private val ROLL_KEY = PluginUtil.createNamespacedKey("roll")

object PersistentRolledAttribute : PersistentDataType<PersistentDataContainer, RolledAttribute> {
    override fun getPrimitiveType() = PersistentDataContainer::class.java

    override fun getComplexType() = RolledAttribute::class.java

    override fun toPrimitive(complex: RolledAttribute, context: PersistentDataAdapterContext): PersistentDataContainer {
        val container = context.newPersistentDataContainer()

        container.set(TYPE_KEY, PersistentEnum(AttributeType::class), complex.type)
        container.set(ROLL_KEY, PersistentDataType.DOUBLE, complex.roll)

        return container
    }

    override fun fromPrimitive(primitive: PersistentDataContainer, context: PersistentDataAdapterContext): RolledAttribute {
        val type = primitive.get(TYPE_KEY, PersistentEnum(AttributeType::class))!!
        val roll = primitive.get(ROLL_KEY, PersistentDataType.DOUBLE)!!

        return RolledAttribute(type, roll)
    }
}