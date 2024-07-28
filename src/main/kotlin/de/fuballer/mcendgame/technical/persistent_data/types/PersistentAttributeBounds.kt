package de.fuballer.mcendgame.technical.persistent_data.types

import de.fuballer.mcendgame.component.item.attribute.data.AttributeBounds
import de.fuballer.mcendgame.component.item.attribute.data.DoubleBounds
import de.fuballer.mcendgame.component.item.attribute.data.IntBounds
import de.fuballer.mcendgame.component.item.attribute.data.StringBounds
import de.fuballer.mcendgame.technical.persistent_data.types.generic.PersistentEnum
import de.fuballer.mcendgame.technical.persistent_data.types.generic.PersistentList
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

private val TYPE_KEY = PluginUtil.createNamespacedKey("type")
private val MIN_KEY = PluginUtil.createNamespacedKey("min")
private val MAX_KEY = PluginUtil.createNamespacedKey("max")
private val OPTIONS_KEY = PluginUtil.createNamespacedKey("options")

private enum class AttributeBoundsType {
    DOUBLE,
    STRING,
    INT
}

object PersistentAttributeBounds : PersistentDataType<PersistentDataContainer, AttributeBounds<*>> {
    override fun getPrimitiveType() = PersistentDataContainer::class.java

    override fun getComplexType() = AttributeBounds::class.java

    override fun fromPrimitive(primitive: PersistentDataContainer, context: PersistentDataAdapterContext): AttributeBounds<*> {
        val type = primitive.get(TYPE_KEY, PersistentEnum(AttributeBoundsType::class))!!

        return when (type) {
            AttributeBoundsType.DOUBLE -> {
                val min = primitive.get(MIN_KEY, PersistentDataType.DOUBLE)!!
                val max = primitive.get(MAX_KEY, PersistentDataType.DOUBLE)!!
                DoubleBounds(min, max)
            }

            AttributeBoundsType.STRING -> {
                val options = primitive.get(OPTIONS_KEY, PersistentList(PersistentDataType.STRING))!!
                StringBounds(options)
            }

            AttributeBoundsType.INT -> {
                val min = primitive.get(MIN_KEY, PersistentDataType.INTEGER)!!
                val max = primitive.get(MAX_KEY, PersistentDataType.INTEGER)!!
                IntBounds(min, max)
            }
        }
    }

    override fun toPrimitive(complex: AttributeBounds<*>, context: PersistentDataAdapterContext): PersistentDataContainer {
        val container = context.newPersistentDataContainer()

        when (complex) {
            is DoubleBounds -> {
                container.set(TYPE_KEY, PersistentEnum(AttributeBoundsType::class), AttributeBoundsType.DOUBLE)
                container.set(MIN_KEY, PersistentDataType.DOUBLE, complex.min)
                container.set(MAX_KEY, PersistentDataType.DOUBLE, complex.max)
            }

            is StringBounds -> {
                container.set(TYPE_KEY, PersistentEnum(AttributeBoundsType::class), AttributeBoundsType.STRING)
                container.set(OPTIONS_KEY, PersistentList(PersistentDataType.STRING), complex.options)
            }

            is IntBounds -> {
                container.set(TYPE_KEY, PersistentEnum(AttributeBoundsType::class), AttributeBoundsType.INT)
                container.set(MIN_KEY, PersistentDataType.INTEGER, complex.min)
                container.set(MAX_KEY, PersistentDataType.INTEGER, complex.max)
            }

            else -> {
                throw IllegalStateException("Invalid ${AttributeBounds::class.simpleName} instance")
            }
        }

        return container
    }
}