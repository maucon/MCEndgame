package de.fuballer.mcendgame.component.custom_entity

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolManager
import com.comphenix.protocol.events.PacketEvent
import de.fuballer.mcendgame.framework.annotation.Component
import de.fuballer.mcendgame.framework.stereotype.LifeCycleListener
import de.fuballer.mcendgame.technical.packet.SendingPacketAdapter
import de.fuballer.mcendgame.util.extension.EntityExtension.isHideEquipment
import org.bukkit.plugin.java.JavaPlugin

@Component
class CustomEntityPacketManager(
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

            if (entity.isHideEquipment()) {
                event.isCancelled = true
            }
        }
}
