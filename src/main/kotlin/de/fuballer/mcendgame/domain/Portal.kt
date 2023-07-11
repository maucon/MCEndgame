package de.fuballer.mcendgame.domain

import de.fuballer.mcendgame.MCEndgame
import de.fuballer.mcendgame.component.dungeon.world.WorldSettings
import de.fuballer.mcendgame.component.mapdevice.MapDeviceSettings
import de.fuballer.mcendgame.helper.WorldHelper
import org.bukkit.*
import org.bukkit.Particle.DustOptions
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlot
import java.util.*

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

        portalEntity = (portalLocationWorld.spawnEntity(portalLocation, EntityType.ARMOR_STAND) as ArmorStand)
            .apply {
                isInvulnerable = true
                setGravity(false)
                isVisible = false
                setAI(false)
                customName = MapDeviceSettings.MAP_DEVICE_PORTAL_ENTITY_NAME
                addEquipmentLock(EquipmentSlot.HEAD, ArmorStand.LockType.ADDING)
                addEquipmentLock(EquipmentSlot.CHEST, ArmorStand.LockType.ADDING)
                addEquipmentLock(EquipmentSlot.LEGS, ArmorStand.LockType.ADDING)
                addEquipmentLock(EquipmentSlot.FEET, ArmorStand.LockType.ADDING)
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

        val world = teleportationTargetLocation.world ?: return

        if (WorldHelper.isDungeonWorld(world)) {
            teleportPlayerAndSetGameMode(player, teleportationTargetLocation, GameMode.SURVIVAL, GameMode.ADVENTURE)
        } else if (WorldSettings.DEFAULT_WORLD_NAMES.contains(world.name)) {
            teleportPlayerAndSetGameMode(player, teleportationTargetLocation, GameMode.ADVENTURE, GameMode.SURVIVAL)
        }
    }

    fun close() {
        if (!active) return
        active = false

        portalEntity!!.remove()
        portalEntityId = null

        Bukkit.getScheduler().cancelTask(particleTaskId)
    }

    private fun startParticleTask() =
        Bukkit.getScheduler().scheduleSyncRepeatingTask(
            MCEndgame.PLUGIN,
            {
                portalLocationWorld.spawnParticle(
                    Particle.REDSTONE,
                    portalLocation.x, portalLocation.y + 1, portalLocation.z,
                    2, 0.2, 0.35, 0.2, 0.0001,
                    DustOptions(Color.PURPLE, 1f)
                )
            },
            0,
            1
        )

    private fun teleportPlayerAndSetGameMode(
        player: Player,
        location: Location,
        initialGameMode: GameMode,
        newGameMode: GameMode
    ) {
        player.teleport(location)

        if (player.gameMode == initialGameMode) {
            player.gameMode = newGameMode
        }
    }
}
