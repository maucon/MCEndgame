package de.fuballer.mcendgame.component.portal.db

import de.fuballer.mcendgame.component.portal.skins.DefaultPortalSkin
import de.fuballer.mcendgame.component.portal.skins.PortalSkin
import de.fuballer.mcendgame.framework.stereotype.Entity
import de.fuballer.mcendgame.util.SchedulingUtil
import de.fuballer.mcendgame.util.WorldUtil
import de.fuballer.mcendgame.util.extension.EntityExtension.setIsPortal
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.inventory.EquipmentSlot
import java.util.*

class Portal(
    private var location: Location,
    val target: Location,
    isInitiallyActive: Boolean = false,
    val isSingleUse: Boolean = false,
    private val skin: PortalSkin = DefaultPortalSkin()
) : Entity<UUID> {
    override var id: UUID

    private var status = PortalStatus.CREATED

    init {
        skin.prepare(location)

        val offsetLocation = location.clone()
        offsetLocation.y = -66.0

        val portalEntity = (location.world!!.spawnEntity(offsetLocation, EntityType.ARMOR_STAND, false) as ArmorStand)

        portalEntity.apply {
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

        id = portalEntity.uniqueId

        if (isInitiallyActive) open()
    }

    fun open() {
        if (status != PortalStatus.CREATED) return
        status = PortalStatus.OPEN

        val entity = WorldUtil.getEntity(location.world!!, id)

        SchedulingUtil.scheduleSyncDelayedTask {
            entity?.teleport(location)
            skin.play()
        }
    }

    fun close() {
        if (status != PortalStatus.OPEN) return
        status = PortalStatus.CLOSED

        skin.cancel()

        val entity = WorldUtil.getEntity(location.world!!, id) ?: return
        entity.remove()
    }
}