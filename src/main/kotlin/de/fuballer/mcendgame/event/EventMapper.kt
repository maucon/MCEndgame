package de.fuballer.mcendgame.event

import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.GameRule
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockDispenseArmorEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemDamageEvent
import org.bukkit.inventory.meta.ArmorMeta
import org.bukkit.inventory.meta.Damageable

@Component
class EventMapper : Listener {
    @EventHandler
    fun on(event: EntityDeathEvent) {
        val entity = event.entity
        val world = entity.world
        if (!world.isDungeonWorld()) return

        val mappedEvent = DungeonEntityDeathEvent(event.entity, event.drops)
        EventGateway.apply(mappedEvent)

        event.drops.clear()
        event.drops.addAll(mappedEvent.drops)
    }

    @EventHandler(ignoreCancelled = true)
    fun on(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return

        if (event.action == InventoryAction.MOVE_TO_OTHER_INVENTORY) { //works in survival (in creative its PLACE_ALL for whatever reason)
            val inventoryType = event.inventory.type
            if (inventoryType != InventoryType.CRAFTING && inventoryType != InventoryType.PLAYER) return

            val item = event.currentItem ?: return
            if (item.itemMeta !is ArmorMeta) return

            applyArmorChangeEvent(player)
            return
        }

        if (event.slotType != InventoryType.SlotType.ARMOR) return
        applyArmorChangeEvent(player)
    }

    @EventHandler(ignoreCancelled = true)
    fun on(event: InventoryDragEvent) {
        for (slot in event.rawSlots) {
            if (slot < 5 || slot > 8) continue

            val player = event.whoClicked as? Player ?: return
            applyArmorChangeEvent(player)
            return
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun on(event: PlayerInteractEvent) {
        val action = event.action
        if (action != Action.RIGHT_CLICK_BLOCK && action != Action.RIGHT_CLICK_AIR) return //RIGHT_CLICK_AIR never fires?

        val item = event.item ?: return
        if (item.itemMeta !is ArmorMeta && item.type != Material.ELYTRA) return

        applyArmorChangeEvent(event.player)
    }


    @EventHandler(ignoreCancelled = true)
    fun on(event: PlayerItemDamageEvent) {
        val item = event.item
        val meta = item.itemMeta as? Damageable ?: return
        val maxDurability = item.type.maxDurability

        if (meta.damage + event.damage < maxDurability) return

        applyArmorChangeEvent(event.player)
    }

    @EventHandler(ignoreCancelled = true)
    fun on(event: PlayerDeathEvent) {
        val player = event.entity
        val world = player.world
        if (world.getGameRuleValue(GameRule.KEEP_INVENTORY) == true) return

        applyArmorChangeEvent(player)
    }

    @EventHandler(ignoreCancelled = true)
    fun on(event: BlockDispenseArmorEvent) {
        val player = event.targetEntity as? Player ?: return
        applyArmorChangeEvent(player)
    }

    private fun applyArmorChangeEvent(player: Player) {
        val mappedEvent = PlayerArmorChangeEvent(player)
        EventGateway.apply(mappedEvent)
    }
}