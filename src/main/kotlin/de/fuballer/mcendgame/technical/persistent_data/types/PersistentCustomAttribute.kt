package de.fuballer.mcendgame.technical.persistent_data.types

import de.fuballer.mcendgame.component.item.attribute.AttributeType
import de.fuballer.mcendgame.component.item.attribute.data.*
import de.fuballer.mcendgame.technical.persistent_data.types.generic.PersistentEnum
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

private val ROLL_TYPE_KEY = PluginUtil.createNamespacedKey("roll_type")

object PersistentCustomAttribute : PersistentDataType<PersistentDataContainer, CustomAttribute> {
    override fun getPrimitiveType() = PersistentDataContainer::class.java

    override fun getComplexType() = CustomAttribute::class.java

    override fun toPrimitive(complex: CustomAttribute, context: PersistentDataAdapterContext): PersistentDataContainer {
        val container = context.newPersistentDataContainer()

        container.set(ROLL_TYPE_KEY, PersistentEnum(RollType::class), complex.rollType)

        return when (complex.rollType) {
            RollType.STATIC -> PersistentStaticAttribute.toPrimitive(complex as StaticAttribute, container)
            RollType.SINGLE -> PersistentSingleValueAttribute.toPrimitive(complex as SingleValueAttribute, container)
        }
    }

    override fun fromPrimitive(primitive: PersistentDataContainer, context: PersistentDataAdapterContext): CustomAttribute {
        val rollType = primitive.get(ROLL_TYPE_KEY, PersistentEnum(RollType::class))!!

        return when (rollType) {
            RollType.STATIC -> PersistentStaticAttribute.fromPrimitive(primitive)
            RollType.SINGLE -> PersistentSingleValueAttribute.fromPrimitive(primitive)
        }
    }
}

object PersistentStaticAttribute {
    private val TYPE_KEY = PluginUtil.createNamespacedKey("type")

    fun toPrimitive(complex: StaticAttribute, container: PersistentDataContainer): PersistentDataContainer {
        container.set(TYPE_KEY, PersistentEnum(AttributeType::class), complex.type)

        return container
    }

    fun fromPrimitive(primitive: PersistentDataContainer): StaticAttribute {
        val type = primitive.get(TYPE_KEY, PersistentEnum(AttributeType::class))!!

        return StaticAttribute(type)
    }
}

object PersistentSingleValueAttribute {
    private val TYPE_KEY = PluginUtil.createNamespacedKey("type")
    private val MIN_KEY = PluginUtil.createNamespacedKey("min")
    private val MAX_KEY = PluginUtil.createNamespacedKey("max")
    private val PERCENT_ROLL_KEY = PluginUtil.createNamespacedKey("percent_roll")

    fun toPrimitive(complex: SingleValueAttribute, container: PersistentDataContainer): PersistentDataContainer {
        container.set(TYPE_KEY, PersistentEnum(AttributeType::class), complex.type)
        container.set(MIN_KEY, PersistentDataType.DOUBLE, complex.bounds.min)
        container.set(MAX_KEY, PersistentDataType.DOUBLE, complex.bounds.max)
        container.set(PERCENT_ROLL_KEY, PersistentDataType.DOUBLE, complex.percentRoll)

        return container
    }

    fun fromPrimitive(primitive: PersistentDataContainer): SingleValueAttribute {
        val type = primitive.get(TYPE_KEY, PersistentEnum(AttributeType::class))!!
        val min = primitive.get(MIN_KEY, PersistentDataType.DOUBLE)!!
        val max = primitive.get(MAX_KEY, PersistentDataType.DOUBLE)!!
        val percentRoll = primitive.get(PERCENT_ROLL_KEY, PersistentDataType.DOUBLE)!!

        val bounds = AttributeBounds(min, max)
        return SingleValueAttribute(type, bounds, percentRoll)
    }
}