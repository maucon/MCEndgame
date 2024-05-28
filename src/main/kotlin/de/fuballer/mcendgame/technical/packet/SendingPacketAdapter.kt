package de.fuballer.mcendgame.technical.packet

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import de.fuballer.mcendgame.configuration.PluginConfiguration

class SendingPacketAdapter(
    packetType: PacketType,
    listenerPriority: ListenerPriority = ListenerPriority.NORMAL,
    private val listener: (event: PacketEvent) -> Unit
) : PacketAdapter(
    PluginConfiguration.plugin(),
    listenerPriority,
    packetType
) {
    override fun onPacketSending(event: PacketEvent) {
        listener.invoke(event)
    }
}