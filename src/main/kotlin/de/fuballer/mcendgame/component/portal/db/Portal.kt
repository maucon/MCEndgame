package de.fuballer.mcendgame.component.portal.db

import de.fuballer.mcendgame.component.portal.skins.DefaultPortalSkin
import de.fuballer.mcendgame.component.portal.skins.PortalSkin
import de.fuballer.mcendgame.util.PluginUtil
import de.fuballer.mcendgame.util.extension.EntityExtension.setIsPortal
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.inventory.EquipmentSlot
import java.util.*

class Portal(
    private var location: Location,
    val target: Location,
    isInitiallyActive: Boolean = false,
    val isSingleUse: Boolean = false,
    private val skin: PortalSkin = DefaultPortalSkin()
) : de.fuballer.mcendgame.framework.stereotype.Entity<UUID> {
    override var id: UUID

    private var status = PortalStatus.CREATED
    var entity: Entity

    init {
        skin.prepare(location)

        val offsetLocation = location.clone()
        offsetLocation.y = -66.0

        entity = (location.world!!.spawnEntity(offsetLocation, EntityType.ARMOR_STAND, false) as ArmorStand)
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
            }.also {
                id = it.uniqueId
            }

        if (isInitiallyActive) open()
    }

    fun open() {
        if (status != PortalStatus.CREATED) return
        status = PortalStatus.OPEN

        PluginUtil.scheduleSyncDelayedTask({
            entity.teleport(location)
            skin.play()
        }, 0)
    }

    fun close() {
        if (status != PortalStatus.OPEN) return
        status = PortalStatus.CLOSED

        entity.remove()
        skin.cancel()
    }
}