package de.fuballer.mcendgame.technical.persistent_data.types.attribute

import de.fuballer.mcendgame.component.item.attribute.data.*
import de.fuballer.mcendgame.technical.persistent_data.types.generic.PersistentEnum
import de.fuballer.mcendgame.util.PluginUtil
import de.fuballer.mcendgame.util.extension.AttributeRollExtension.run
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

private val TYPE_KEY = PluginUtil.createNamespacedKey("type")
private val BOUNDS_KEY = PluginUtil.createNamespacedKey("bounds")
private val PERCENT_ROLL_KEY = PluginUtil.createNamespacedKey("percent_roll")
private val INDEX_ROLL_KEY = PluginUtil.createNamespacedKey("index_roll")

private enum class AttributeRollType {
    DOUBLE_ROLL,
    STRING_ROLL,
    INT_ROLL
}

object PersistentAttributeRoll : PersistentDataType<PersistentDataContainer, AttributeRoll<*>> {
    override fun getPrimitiveType() = PersistentDataContainer::class.java

    override fun getComplexType() = AttributeRoll::class.java

    override fun fromPrimitive(primitive: PersistentDataContainer, context: PersistentDataAdapterContext): AttributeRoll<*> {
        val type = primitive.get(TYPE_KEY, PersistentEnum(AttributeRollType::class))!!
        val bounds = primitive.get(BOUNDS_KEY, PersistentAttributeBounds)!!

        return when (type) {
            AttributeRollType.DOUBLE_ROLL -> {
                val percentRoll = primitive.get(PERCENT_ROLL_KEY, PersistentDataType.DOUBLE)!!
                DoubleRoll(bounds as DoubleBounds, percentRoll)
            }

            AttributeRollType.STRING_ROLL -> {
                val indexRoll = primitive.get(INDEX_ROLL_KEY, PersistentDataType.INTEGER)!!
                StringRoll(bounds as StringBounds, indexRoll)
            }

            AttributeRollType.INT_ROLL -> {
                val percentRoll = primitive.get(PERCENT_ROLL_KEY, PersistentDataType.DOUBLE)!!
                IntRoll(bounds as IntBounds, percentRoll)
            }
        }
    }

    override fun toPrimitive(complex: AttributeRoll<*>, context: PersistentDataAdapterContext): PersistentDataContainer {
        val container = context.newPersistentDataContainer()
        container.set(BOUNDS_KEY, PersistentAttributeBounds, complex.bounds)

        complex.run(
            {
                container.set(TYPE_KEY, PersistentEnum(AttributeRollType::class), AttributeRollType.DOUBLE_ROLL)
                container.set(PERCENT_ROLL_KEY, PersistentDataType.DOUBLE, it.percentRoll)
            },
            {
                container.set(TYPE_KEY, PersistentEnum(AttributeRollType::class), AttributeRollType.STRING_ROLL)
                container.set(INDEX_ROLL_KEY, PersistentDataType.INTEGER, it.indexRoll)
            },
            {
                container.set(TYPE_KEY, PersistentEnum(AttributeRollType::class), AttributeRollType.INT_ROLL)
                container.set(PERCENT_ROLL_KEY, PersistentDataType.DOUBLE, it.percentRoll)
            }
        )

        return container
    }
}