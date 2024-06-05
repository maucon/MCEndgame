package de.fuballer.mcendgame.component.cosmetics.player

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolManager
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot
import com.comphenix.protocol.wrappers.Pair
import de.fuballer.mcendgame.component.cosmetics.player.db.PlayerCosmeticsRepository
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.LifeCycleListener
import de.fuballer.mcendgame.technical.packet.SendingPacketAdapter
import org.bukkit.Server
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

private const val PLAYER_INVENTORY_ID = 0

@Component
class PlayerCosmeticsPacketManager(
    private val playerCosmeticsRepository: PlayerCosmeticsRepository,
    private val protocolManager: ProtocolManager,
    private val server: Server
) : LifeCycleListener {
    override fun initialize(plugin: JavaPlugin) {
        val adapter = listOf(
            modifyEntityEquipmentPacket(),
            modifyWindowItemsPacket(),
            modifySetSlotPacket(),
        )

        adapter.forEach { protocolManager.addPacketListener(it) }
    }

    private fun modifySetSlotPacket() =
        SendingPacketAdapter(
            PacketType.Play.Server.SET_SLOT
        ) { event: PacketEvent ->
            val windowId = event.packet.integers.read(0)
            if (windowId != PLAYER_INVENTORY_ID) return@SendingPacketAdapter

            val player = event.player
            val playerCosmeticsEntity = playerCosmeticsRepository.findById(player.uniqueId) ?: return@SendingPacketAdapter

            val slot = event.packet.integers.read(2)
            when (slot) {
                5 -> event.packet.itemModifier.write(0, playerCosmeticsEntity.getDisplayedItem(player, EquipmentSlot.HEAD))
                6 -> event.packet.itemModifier.write(0, playerCosmeticsEntity.getDisplayedItem(player, EquipmentSlot.CHEST))
                7 -> event.packet.itemModifier.write(0, playerCosmeticsEntity.getDisplayedItem(player, EquipmentSlot.LEGS))
                8 -> event.packet.itemModifier.write(0, playerCosmeticsEntity.getDisplayedItem(player, EquipmentSlot.FEET))
            }
        }

    private fun modifyWindowItemsPacket() =
        SendingPacketAdapter(
            PacketType.Play.Server.WINDOW_ITEMS
        ) { event: PacketEvent ->
            val windowId = event.packet.integers.read(0)
            if (windowId != PLAYER_INVENTORY_ID) return@SendingPacketAdapter

            val items = event.packet.itemListModifier.read(0)

            val player = event.player
            val playerCosmeticsEntity = playerCosmeticsRepository.findById(player.uniqueId) ?: return@SendingPacketAdapter

            items[5] = playerCosmeticsEntity.getDisplayedItem(player, EquipmentSlot.HEAD)
            items[6] = playerCosmeticsEntity.getDisplayedItem(player, EquipmentSlot.CHEST)
            items[7] = playerCosmeticsEntity.getDisplayedItem(player, EquipmentSlot.LEGS)
            items[8] = playerCosmeticsEntity.getDisplayedItem(player, EquipmentSlot.FEET)

            event.packet.itemListModifier.write(0, items)
        }

    private fun modifyEntityEquipmentPacket() =
        SendingPacketAdapter(
            PacketType.Play.Server.ENTITY_EQUIPMENT
        ) { event: PacketEvent ->
            val entityId = event.packet.integers.read(0)

            val player = server.onlinePlayers.firstOrNull { it.entityId == entityId } ?: return@SendingPacketAdapter
            val playerCosmeticsEntity = playerCosmeticsRepository.findById(player.uniqueId) ?: return@SendingPacketAdapter

            val itemList = mutableListOf<Pair<ItemSlot, ItemStack>>()

            itemList.add(Pair(ItemSlot.HEAD, playerCosmeticsEntity.getDisplayedItem(player, EquipmentSlot.HEAD)))
            itemList.add(Pair(ItemSlot.CHEST, playerCosmeticsEntity.getDisplayedItem(player, EquipmentSlot.CHEST)))
            itemList.add(Pair(ItemSlot.LEGS, playerCosmeticsEntity.getDisplayedItem(player, EquipmentSlot.LEGS)))
            itemList.add(Pair(ItemSlot.FEET, playerCosmeticsEntity.getDisplayedItem(player, EquipmentSlot.FEET)))

            event.packet.slotStackPairLists.write(0, itemList)
        }
}
