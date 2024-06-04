package de.fuballer.mcendgame.component.cosmetics.player

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolManager
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot
import com.comphenix.protocol.wrappers.Pair
import de.fuballer.mcendgame.component.cosmetics.player.db.PlayerCosmeticsRepository
import de.fuballer.mcendgame.component.inventory.CustomInventoryType
import de.fuballer.mcendgame.component.item.equipment.Equipment
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.util.extension.EventExtension.cancel
import de.fuballer.mcendgame.util.extension.InventoryExtension.getCustomType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.EquipmentSlot

@Component
class PlayerCosmeticsService(
    private val playerCosmeticsRepository: PlayerCosmeticsRepository,
    private val protocolManager: ProtocolManager
) : Listener {
    @EventHandler
    fun on(event: InventoryClickEvent) {
        val cosmeticsInventory = event.inventory
        if (cosmeticsInventory.getCustomType() != CustomInventoryType.COSMETICS) return

        event.cancel()

        val clickedInventory = event.clickedInventory ?: return

        val action = event.action
        if (action != InventoryAction.MOVE_TO_OTHER_INVENTORY && action != InventoryAction.PICKUP_ALL) return

        val player = event.whoClicked as Player
        val cosmeticsEntity = playerCosmeticsRepository.findById(player.uniqueId) ?: return

        when (event.rawSlot) {
            PlayerCosmeticsSettings.SHOW_HELMET_INVENTORY_INDEX -> {
                cosmeticsEntity.showHelmet = !cosmeticsEntity.showHelmet
                cosmeticsInventory.setItem(PlayerCosmeticsSettings.SHOW_HELMET_INVENTORY_INDEX, cosmeticsEntity.getShowHelmetItem())
            }

            PlayerCosmeticsSettings.HELMET_INVENTORY_INDEX -> {
                cosmeticsEntity.helmet = null
                cosmeticsInventory.setItem(PlayerCosmeticsSettings.HELMET_INVENTORY_INDEX, null)
            }

            PlayerCosmeticsSettings.CHESTPLATE_INVENTORY_INDEX -> {
                cosmeticsEntity.chestplate = null
                cosmeticsInventory.setItem(PlayerCosmeticsSettings.CHESTPLATE_INVENTORY_INDEX, null)
            }

            PlayerCosmeticsSettings.LEGGINGS_INVENTORY_INDEX -> {
                cosmeticsEntity.leggings = null
                cosmeticsInventory.setItem(PlayerCosmeticsSettings.LEGGINGS_INVENTORY_INDEX, null)
            }

            PlayerCosmeticsSettings.BOOTS_INVENTORY_INDEX -> {
                cosmeticsEntity.boots = null
                cosmeticsInventory.setItem(PlayerCosmeticsSettings.BOOTS_INVENTORY_INDEX, null)
            }

            else -> {
                val item = clickedInventory.getItem(event.slot) ?: return
                val material = item.type
                val equipment = Equipment.fromMaterial(material) ?: return

                when (equipment.slot) {
                    EquipmentSlot.HEAD -> {
                        cosmeticsEntity.helmet = item.clone()
                        cosmeticsInventory.setItem(PlayerCosmeticsSettings.HELMET_INVENTORY_INDEX, item.clone())
                    }

                    EquipmentSlot.CHEST -> {
                        cosmeticsEntity.chestplate = item.clone()
                        cosmeticsInventory.setItem(PlayerCosmeticsSettings.CHESTPLATE_INVENTORY_INDEX, item.clone())
                    }

                    EquipmentSlot.LEGS -> {
                        cosmeticsEntity.leggings = item.clone()
                        cosmeticsInventory.setItem(PlayerCosmeticsSettings.LEGGINGS_INVENTORY_INDEX, item.clone())
                    }

                    EquipmentSlot.FEET -> {
                        cosmeticsEntity.boots = item.clone()
                        cosmeticsInventory.setItem(PlayerCosmeticsSettings.BOOTS_INVENTORY_INDEX, item.clone())
                    }

                    else -> return
                }
            }
        }

        sendCosmeticsPacket(player)
    }

    fun sendCosmeticsPacket(player: Player) {
        val playerCosmeticsEntity = playerCosmeticsRepository.findById(player.uniqueId) ?: return

        val cosmeticEquipment = mutableListOf(
            Pair(ItemSlot.HEAD, playerCosmeticsEntity.getHelmetItemOrAir(false)),
            Pair(ItemSlot.CHEST, playerCosmeticsEntity.getChestplateItemOrAir()),
            Pair(ItemSlot.LEGS, playerCosmeticsEntity.getLeggingsItemOrAir()),
            Pair(ItemSlot.FEET, playerCosmeticsEntity.getBootsItemOrAir()),
        )

        val equipmentPacket = protocolManager.createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT)

        equipmentPacket.integers.write(0, player.entityId)
        equipmentPacket.slotStackPairLists.write(0, cosmeticEquipment)

        try {
            protocolManager.sendServerPacket(player, equipmentPacket)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}