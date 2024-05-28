package de.fuballer.mcendgame.technical.persistent_data.types

import de.fuballer.mcendgame.component.dungeon.modifier.Modifier
import de.fuballer.mcendgame.component.dungeon.modifier.ModifierOperation
import de.fuballer.mcendgame.component.dungeon.modifier.ModifierType
import de.fuballer.mcendgame.technical.persistent_data.types.generic.PersistentEnum
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

private val TYPE_KEY = PluginUtil.createNamespacedKey("type")
private val OPERATION_KEY = PluginUtil.createNamespacedKey("operation")
private val VALUE_KEY = PluginUtil.createNamespacedKey("value")

object PersistentModifier : PersistentDataType<PersistentDataContainer, Modifier> {
    override fun getPrimitiveType() = PersistentDataContainer::class.java

    override fun getComplexType() = Modifier::class.java

    override fun toPrimitive(complex: Modifier, context: PersistentDataAdapterContext): PersistentDataContainer {
        val container = context.newPersistentDataContainer()

        container.set(TYPE_KEY, PersistentEnum(ModifierType::class), complex.type)
        container.set(OPERATION_KEY, PersistentEnum(ModifierOperation::class), complex.operation)
        container.set(VALUE_KEY, PersistentDataType.DOUBLE, complex.value)

        return container
    }

    override fun fromPrimitive(primitive: PersistentDataContainer, context: PersistentDataAdapterContext): Modifier {
        val type = primitive.get(TYPE_KEY, PersistentEnum(ModifierType::class))!!
        val operation = primitive.get(OPERATION_KEY, PersistentEnum(ModifierOperation::class))!!
        val value = primitive.get(VALUE_KEY, PersistentDataType.DOUBLE)!!

        return Modifier(type, operation, value)
    }
}