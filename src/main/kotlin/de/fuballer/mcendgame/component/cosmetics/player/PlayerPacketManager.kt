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
import org.bukkit.Material
import org.bukkit.Server
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

@Component
class PlayerPacketManager(
    private val playerCosmeticsRepository: PlayerCosmeticsRepository,
    private val protocolManager: ProtocolManager,
    private val server: Server
) : LifeCycleListener {
    override fun initialize(plugin: JavaPlugin) {
        val adapter = listOf(
            changeEquipmentAdapter(),
            c(),
        )

        adapter.forEach { protocolManager.addPacketListener(it) }

    }

    private fun c() =
        SendingPacketAdapter(
            PacketType.Play.Server.WINDOW_ITEMS
        ) { event: PacketEvent ->
            val windowId = event.packet.integers.read(0)
            if (windowId != 0) return@SendingPacketAdapter

            val items = event.packet.itemListModifier.read(0)

            val player = event.player
            val playerEquipment = player.equipment
            val playerCosmeticsEntity = playerCosmeticsRepository.findById(player.uniqueId) ?: return@SendingPacketAdapter

            if (playerCosmeticsEntity.helmet != null) {
                items[5] = playerCosmeticsEntity.getHelmetItemOrAir(false)
            } else {
                if (playerCosmeticsEntity.showHelmet) {
                    items[5] = playerEquipment?.helmet ?: ItemStack(Material.AIR)
                } else {
                    items[5] = ItemStack(Material.AIR)
                }
            }
            if (playerCosmeticsEntity.chestplate != null) {
                items[6] = playerCosmeticsEntity.getChestplateItemOrAir()
            } else {
                items[6] = playerEquipment?.chestplate ?: ItemStack(Material.AIR)
            }
            if (playerCosmeticsEntity.leggings != null) {
                items[7] = playerCosmeticsEntity.getLeggingsItemOrAir()
            } else {
                items[7] = playerEquipment?.leggings ?: ItemStack(Material.AIR)
            }
            if (playerCosmeticsEntity.boots != null) {
                items[8] = playerCosmeticsEntity.getBootsItemOrAir()
            } else {
                items[8] = playerEquipment?.boots ?: ItemStack(Material.AIR)
            }

            event.packet.itemListModifier.write(0, items)
        }

    private fun changeEquipmentAdapter() =
        SendingPacketAdapter(
            PacketType.Play.Server.ENTITY_EQUIPMENT
        ) { event: PacketEvent ->
            val entityId = event.packet.integers.read(0)

            val player = server.onlinePlayers.firstOrNull { it.entityId == entityId } ?: return@SendingPacketAdapter

            val playerEquipment = player.equipment
            val playerCosmeticsEntity = playerCosmeticsRepository.findById(player.uniqueId) ?: return@SendingPacketAdapter

            val itemList = mutableListOf<Pair<ItemSlot, ItemStack>>()

            if (playerCosmeticsEntity.showHelmet) {
                if (playerCosmeticsEntity.helmet != null) {
                    itemList.add(Pair(ItemSlot.HEAD, playerCosmeticsEntity.getHelmetItemOrAir()))
                } else {
                    itemList.add(Pair(ItemSlot.HEAD, playerEquipment?.helmet ?: ItemStack(Material.AIR)))
                }
            } else {
                itemList.add(Pair(ItemSlot.HEAD, ItemStack(Material.AIR)))
            }

            if (playerCosmeticsEntity.chestplate != null) {
                itemList.add(Pair(ItemSlot.CHEST, playerCosmeticsEntity.getChestplateItemOrAir()))
            } else {
                itemList.add(Pair(ItemSlot.CHEST, playerEquipment?.chestplate ?: ItemStack(Material.AIR)))
            }

            if (playerCosmeticsEntity.leggings != null) {
                itemList.add(Pair(ItemSlot.LEGS, playerCosmeticsEntity.getLeggingsItemOrAir()))
            } else {
                itemList.add(Pair(ItemSlot.LEGS, playerEquipment?.leggings ?: ItemStack(Material.AIR)))
            }

            if (playerCosmeticsEntity.boots != null) {
                itemList.add(Pair(ItemSlot.FEET, playerCosmeticsEntity.getBootsItemOrAir()))
            } else {
                itemList.add(Pair(ItemSlot.FEET, playerEquipment?.boots ?: ItemStack(Material.AIR)))
            }

            event.packet.slotStackPairLists.write(0, itemList)
        }
}
