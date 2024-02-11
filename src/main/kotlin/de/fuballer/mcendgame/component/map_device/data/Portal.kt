package de.fuballer.mcendgame.component.map_device.data

import de.fuballer.mcendgame.technical.extension.EntityExtension.setIsPortal
import de.fuballer.mcendgame.util.PluginUtil
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class Portal(
    private val portalLocation: Location,
    val teleportationTargetLocation: Location,
    initialActive: Boolean = false
) {
    private val portalLocationWorld = portalLocation.world!!
    private var particleTaskId = 0
    private var active = false
    private var portalEntity: Entity? = null

    var portalEntityId: UUID? = null
    var teleportationTargetWorld = teleportationTargetLocation.world!!

    init {
        if (initialActive) activate()
    }

    fun activate() {
        if (active) return
        active = true

        portalEntity = (portalLocationWorld.spawnEntity(portalLocation, EntityType.ARMOR_STAND, false) as ArmorStand)
            .apply {
                isInvulnerable = true
                setGravity(false)
                isVisible = false
                setAI(false)

                addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.ADDING)
                addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.ADDING)
                addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.ADDING)
                addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.ADDING)

                setIsPortal()
            }

        portalEntityId = portalEntity!!.uniqueId
        particleTaskId = startParticleTask()
    }

    fun teleportPlayer(
        player: Player,
        closePortal: Boolean
    ) {
        if (closePortal) {
            close()
        }

        player.teleport(teleportationTargetLocation)
    }

    fun close() {
        if (!active) return
        active = false

        portalEntity!!.remove()
        portalEntityId = null

        PluginUtil.cancelTask(particleTaskId)
    }

    // TODO fix portal orientation
    // TODO add infra to change portal skins
    // TODO also add open and close animations for portals
    private fun startParticleTask() =
        PluginUtil.scheduleSyncRepeatingTask(0, 1) {
            val width = 0.75 / 2
            val height = 1.25 / 2
            val particles = 12
            val increment = (2 * Math.PI) / particles

            for (i in 0 until particles) {
                val angle = i * increment
                val x = portalLocation.x + width * cos(angle)
                val y = portalLocation.y + height * sin(angle) + 0.75
                val z = portalLocation.z

                portalLocationWorld.spawnParticle(
                    Particle.PORTAL,
                    x, y, z,
                    1,
                    0.0, 0.1, 0.0,
                    0.1
                )
            }
        }
}