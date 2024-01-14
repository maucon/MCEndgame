package de.fuballer.mcendgame.component.persistent_data.types

import de.fuballer.mcendgame.component.item_attribute.AttributeType
import de.fuballer.mcendgame.component.item_attribute.RolledAttribute
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType

private const val LIST_DELIMITER = "; "
private const val ATTRIBUTE_DELIMITER = ": "

@Suppress("UNCHECKED_CAST")
object PersistentRolledAttributeList : PersistentDataType<String, List<RolledAttribute>> {
    override fun getPrimitiveType(): Class<String> = String::class.java

    override fun getComplexType(): Class<List<RolledAttribute>> = List::class.java as Class<List<RolledAttribute>>

    override fun toPrimitive(complex: List<RolledAttribute>, context: PersistentDataAdapterContext): String {
        return complex.joinToString(LIST_DELIMITER) { "${it.type}$ATTRIBUTE_DELIMITER${it.roll}" }
    }

    override fun fromPrimitive(primitive: String, context: PersistentDataAdapterContext): List<RolledAttribute> {
        if (primitive.isEmpty()) return emptyList()

        return primitive.split(LIST_DELIMITER)
            .map {
                val split = it.split(ATTRIBUTE_DELIMITER)
                val type = AttributeType.valueOf(split[0])
                val roll = split[1].toDouble()

                RolledAttribute(type, roll)
            }
            .toList()
    }
}