package de.fuballer.mcendgame.component.cosmetics.player

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolManager
import de.fuballer.mcendgame.component.cosmetics.player.db.PlayerCosmeticsEntity
import de.fuballer.mcendgame.component.cosmetics.player.db.PlayerCosmeticsRepository
import de.fuballer.mcendgame.component.inventory.CustomInventoryType
import de.fuballer.mcendgame.component.item.equipment.Equipment
import de.fuballer.mcendgame.configuration.PluginConfiguration
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import de.fuballer.mcendgame.util.extension.InventoryExtension.getCustomType
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

@Component
class PlayerCosmeticsService(
    private val playerCosmeticsRepository: PlayerCosmeticsRepository,
    private val protocolManager: ProtocolManager
) : Listener {
    @EventHandler
    fun on(
        event: InventoryClickEvent
    ) {
        val cosmeticsInventory = event.inventory
        if (cosmeticsInventory.getCustomType() != CustomInventoryType.COSMETICS) return
        event.cancel()

        val action = event.action
        if (action != InventoryAction.MOVE_TO_OTHER_INVENTORY && action != InventoryAction.PICKUP_ALL) return

        val player = event.whoClicked as Player
        val cosmeticsEntity = playerCosmeticsRepository.findById(player.uniqueId) ?: return

        when (event.rawSlot) {
            PlayerCosmeticsSettings.SHOW_HELMET_INVENTORY_INDEX -> {
                changeShowHelmet(cosmeticsEntity, cosmeticsInventory)

                updateEquipment(player, EquipmentSlot.HEAD)
            }

            in PlayerCosmeticsSettings.EQUIPMENT_INVENTORY_INDICES.values -> {
                val slot = PlayerCosmeticsSettings.EQUIPMENT_INVENTORY_INDICES.filterValues { it == event.rawSlot }.keys.first()

                removeCosmeticEquipment(cosmeticsEntity, cosmeticsInventory, slot)

                updateEquipment(player, slot)
            }

            else -> {
                val clickedInventory = event.clickedInventory ?: return
                val item = clickedInventory.getItem(event.slot) ?: return
                val equipment = Equipment.fromMaterial(item.type) ?: return
                val slot = equipment.slot

                if (!addCosmeticEquipment(cosmeticsEntity, cosmeticsInventory, item, slot)) return

                updateEquipment(player, slot)
            }
        }
    }

    private fun changeShowHelmet(
        cosmeticsEntity: PlayerCosmeticsEntity,
        cosmeticsInventory: Inventory
    ) {
        cosmeticsEntity.showHelmet = !cosmeticsEntity.showHelmet
        cosmeticsInventory.setItem(PlayerCosmeticsSettings.SHOW_HELMET_INVENTORY_INDEX, cosmeticsEntity.getShowHelmetItem())
    }

    private fun removeCosmeticEquipment(
        cosmeticsEntity: PlayerCosmeticsEntity,
        cosmeticsInventory: Inventory,
        slot: EquipmentSlot,
    ) {
        cosmeticsEntity.cosmeticEquipment[slot] = null
        cosmeticsInventory.setItem(PlayerCosmeticsSettings.EQUIPMENT_INVENTORY_INDICES[slot]!!, null)
    }

    private fun addCosmeticEquipment(
        cosmeticsEntity: PlayerCosmeticsEntity,
        cosmeticsInventory: Inventory,
        item: ItemStack,
        slot: EquipmentSlot,
    ): Boolean {
        cosmeticsEntity.cosmeticEquipment[slot] = item.clone()

        if (!PlayerCosmeticsSettings.EQUIPMENT_INVENTORY_INDICES.containsKey(slot)) return false
        cosmeticsInventory.setItem(PlayerCosmeticsSettings.EQUIPMENT_INVENTORY_INDICES[slot]!!, item.clone())

        return true
    }

    private fun updateEquipment(
        player: Player,
        slot: EquipmentSlot
    ) {
        sendCosmeticsPacket(player)

        val equipment = player.equipment ?: return
        val oldItem = equipment.getItem(slot)

        equipment.setItem(slot, ItemStack(Material.DIRT))

        Bukkit.getScheduler().runTaskLater(PluginConfiguration.INSTANCE, Runnable {
            equipment.setItem(slot, oldItem)
        }, 1L)
    }

    private fun sendCosmeticsPacket(
        player: Player
    ) {
        val equipmentPacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT)

        equipmentPacket.integers.write(0, player.entityId)
        try {
            protocolManager.sendServerPacket(player, equipmentPacket)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}