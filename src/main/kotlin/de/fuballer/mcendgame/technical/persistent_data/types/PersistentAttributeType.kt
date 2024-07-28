package de.fuballer.mcendgame.technical.persistent_data.types

import de.fuballer.mcendgame.component.item.attribute.CustomAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.VanillaAttributeTypes
import de.fuballer.mcendgame.component.item.attribute.data.AttributeType
import de.fuballer.mcendgame.component.item.attribute.data.CustomAttributeType
import de.fuballer.mcendgame.component.item.attribute.data.VanillaAttributeType
import de.fuballer.mcendgame.technical.persistent_data.types.generic.PersistentEnum
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import kotlin.reflect.full.memberProperties

private val TYPE_KEY = PluginUtil.createNamespacedKey("type")
private val MEMBER_KEY = PluginUtil.createNamespacedKey("type")

private enum class AttributeTypeType {
    CUSTOM_ATTRIBUTE_TYPE,
    VANILLA_ATTRIBUTE_TYPE
}

object PersistentAttributeType : PersistentDataType<PersistentDataContainer, AttributeType> {
    override fun getPrimitiveType() = PersistentDataContainer::class.java

    override fun getComplexType() = AttributeType::class.java

    override fun fromPrimitive(primitive: PersistentDataContainer, context: PersistentDataAdapterContext): AttributeType {
        val type = primitive.get(TYPE_KEY, PersistentEnum(AttributeTypeType::class))!!

        return when (type) {
            AttributeTypeType.CUSTOM_ATTRIBUTE_TYPE -> {
                val memberName = primitive.get(MEMBER_KEY, PersistentDataType.STRING)

                CustomAttributeTypes::class.memberProperties
                    .firstOrNull { it.name == memberName }
                    ?.get(CustomAttributeTypes) as CustomAttributeType
            }

            AttributeTypeType.VANILLA_ATTRIBUTE_TYPE -> {
                val memberName = primitive.get(MEMBER_KEY, PersistentDataType.STRING)

                VanillaAttributeTypes::class.memberProperties
                    .firstOrNull { it.name == memberName }
                    ?.get(VanillaAttributeTypes) as VanillaAttributeType
            }
        }
    }

    override fun toPrimitive(complex: AttributeType, context: PersistentDataAdapterContext): PersistentDataContainer {
        val container = context.newPersistentDataContainer()

        when (complex) {
            is CustomAttributeType -> {
                container.set(TYPE_KEY, PersistentEnum(AttributeTypeType::class), AttributeTypeType.CUSTOM_ATTRIBUTE_TYPE)

                val memberName = CustomAttributeTypes::class.memberProperties
                    .firstOrNull { it.get(CustomAttributeTypes) == complex }
                    ?.name!!

                container.set(MEMBER_KEY, PersistentDataType.STRING, memberName)
            }

            is VanillaAttributeType -> {
                container.set(TYPE_KEY, PersistentEnum(AttributeTypeType::class), AttributeTypeType.VANILLA_ATTRIBUTE_TYPE)

                val memberName = VanillaAttributeTypes::class.memberProperties
                    .firstOrNull { it.get(VanillaAttributeTypes) == complex }
                    ?.name!!

                container.set(MEMBER_KEY, PersistentDataType.STRING, memberName)
            }

            else -> {
                throw IllegalStateException("Invalid ${AttributeType::class.simpleName} instance")
            }
        }

        return container
    }
}