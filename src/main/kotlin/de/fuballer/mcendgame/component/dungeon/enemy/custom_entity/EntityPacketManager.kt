package de.fuballer.mcendgame.component.dungeon.enemy.custom_entity

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolManager
import com.comphenix.protocol.events.PacketEvent
import de.fuballer.mcendgame.domain.packet.SendingPacketAdapter
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.LifeCycleListener
import de.fuballer.mcendgame.util.PersistentDataUtil
import org.bukkit.plugin.java.JavaPlugin

@Component
class EntityPacketManager(
    private val protocolManager: ProtocolManager
) : LifeCycleListener {
    override fun initialize(plugin: JavaPlugin) {
        val adapter = listOf(
            hideEquipmentAdapter()
        )

        adapter.forEach { protocolManager.addPacketListener(it) }
    }

    private fun hideEquipmentAdapter() =
        SendingPacketAdapter(
            PacketType.Play.Server.ENTITY_EQUIPMENT
        ) { event: PacketEvent ->
            val world = event.player.world
            val entityId = event.packet.integers.read(0)
            val entity = protocolManager.getEntityFromID(world, entityId) ?: return@SendingPacketAdapter

            val hideEquipment = PersistentDataUtil.getValue(entity, DataTypeKeys.HIDE_EQUIPMENT) ?: return@SendingPacketAdapter

            if (hideEquipment) {
                event.isCancelled = true
            }
        }
}
