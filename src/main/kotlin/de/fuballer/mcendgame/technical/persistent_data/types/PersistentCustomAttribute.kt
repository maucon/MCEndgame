package de.fuballer.mcendgame.technical.persistent_data.types

import de.fuballer.mcendgame.component.item.attribute.data.CustomAttribute
import de.fuballer.mcendgame.technical.persistent_data.types.generic.PersistentList
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

private val TYPE_KEY = PluginUtil.createNamespacedKey("type")
private val ATTRIBUTE_ROLLS_KEY = PluginUtil.createNamespacedKey("attribute_rolls")

object PersistentCustomAttribute : PersistentDataType<PersistentDataContainer, CustomAttribute> {
    override fun getPrimitiveType() = PersistentDataContainer::class.java

    override fun getComplexType() = CustomAttribute::class.java

    override fun toPrimitive(complex: CustomAttribute, context: PersistentDataAdapterContext): PersistentDataContainer {
        val container = context.newPersistentDataContainer()

        container.set(TYPE_KEY, PersistentAttributeType, complex.type)
        container.set(ATTRIBUTE_ROLLS_KEY, PersistentList(PersistentAttributeRoll), complex.attributeRolls)

        return container
    }

    override fun fromPrimitive(primitive: PersistentDataContainer, context: PersistentDataAdapterContext): CustomAttribute {
        val type = primitive.get(TYPE_KEY, PersistentAttributeType)!!
        val attributeRolls = primitive.get(ATTRIBUTE_ROLLS_KEY, PersistentList(PersistentAttributeRoll))!!

        return CustomAttribute(type, attributeRolls)
    }
}