package de.fuballer.mcendgame.component.killer

import de.fuballer.mcendgame.component.inventory.CustomInventoryType
import de.fuballer.mcendgame.component.killer.db.KillerEntity
import de.fuballer.mcendgame.component.killer.db.KillerRepository
import de.fuballer.mcendgame.framework.annotation.Service
import de.fuballer.mcendgame.util.EntityUtil
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import de.fuballer.mcendgame.util.extension.InventoryExtension.getCustomType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.inventory.InventoryClickEvent

@Service
class KillerService(
    private val killerRepo: KillerRepository
) : Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun on(event: EntityDamageByEntityEvent) {
        val damaged = event.entity
        if (damaged !is Player) return

        val damager = EntityUtil.getLivingEntityDamager(event.damager) ?: return

        if (event.finalDamage < damaged.health) return

        val entity = KillerEntity(damaged.uniqueId, damager)
        killerRepo.save(entity)
    }

    @EventHandler
    fun on(event: InventoryClickEvent) {
        if (event.inventory.getCustomType() != CustomInventoryType.KILLER) return

        event.cancel()
    }
}