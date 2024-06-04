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
            a(),
        )

        adapter.forEach { protocolManager.addPacketListener(it) }
    }

    private fun a() =
        SendingPacketAdapter(
            PacketType.Play.Server.ENTITY_METADATA
        ) { event: PacketEvent ->
            val entityId = event.packet.integers.read(0)

            val player = server.onlinePlayers.firstOrNull { it.entityId == entityId } ?: return@SendingPacketAdapter

            val playerEquipment = player.equipment
            val playerCosmeticsEntity = playerCosmeticsRepository.findById(player.uniqueId) ?: return@SendingPacketAdapter

            val modifier = event.packet.watchableCollectionModifier
            modifier.readSafely(0) // TODO FKING FIX IT
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

            if (playerCosmeticsEntity.showHelmet && playerCosmeticsEntity.helmet != null) {
                itemList.add(Pair(ItemSlot.HEAD, playerCosmeticsEntity.getHelmetItemOrAir()))
            } else {
                itemList.add(Pair(ItemSlot.HEAD, playerEquipment?.helmet ?: ItemStack(Material.AIR)))
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
