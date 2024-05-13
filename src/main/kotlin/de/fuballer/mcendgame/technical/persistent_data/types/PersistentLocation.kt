package de.fuballer.mcendgame.technical.persistent_data.types

import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.Location
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

private val X_KEY = PluginUtil.createNamespacedKey("x")
private val Y_KEY = PluginUtil.createNamespacedKey("y")
private val Z_KEY = PluginUtil.createNamespacedKey("z")
private val YAW_KEY = PluginUtil.createNamespacedKey("yaw")
private val PITCH_KEY = PluginUtil.createNamespacedKey("pitch")

object PersistentLocation : PersistentDataType<PersistentDataContainer, Location> {
    override fun getPrimitiveType() = PersistentDataContainer::class.java

    override fun getComplexType() = Location::class.java

    override fun toPrimitive(complex: Location, context: PersistentDataAdapterContext): PersistentDataContainer {
        val container = context.newPersistentDataContainer()

        container.set(X_KEY, PersistentDataType.DOUBLE, complex.x)
        container.set(Y_KEY, PersistentDataType.DOUBLE, complex.y)
        container.set(Z_KEY, PersistentDataType.DOUBLE, complex.z)
        container.set(YAW_KEY, PersistentDataType.FLOAT, complex.yaw)
        container.set(PITCH_KEY, PersistentDataType.FLOAT, complex.pitch)

        return container
    }

    override fun fromPrimitive(primitive: PersistentDataContainer, context: PersistentDataAdapterContext): Location {
        val x = primitive.get(X_KEY, PersistentDataType.DOUBLE)!!
        val y = primitive.get(Y_KEY, PersistentDataType.DOUBLE)!!
        val z = primitive.get(Z_KEY, PersistentDataType.DOUBLE)!!
        val yaw = primitive.get(YAW_KEY, PersistentDataType.FLOAT)!!
        val pitch = primitive.get(PITCH_KEY, PersistentDataType.FLOAT)!!

        return Location(null, x, y, z, yaw, pitch)
    }
}