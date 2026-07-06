package de.fuballer.mcendgame.main.component.dungeon.generation.encounter.encounters.scarred_one.networking

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Uuids
import java.util.*

private val PAYLOAD_ID = IdentifierUtil.default("scarred_one_response")

data class ScarredOneResponsePayload(
    val accept: Boolean,
    val uuid: UUID,
) : CustomPayload {
    companion object {
        val ID = CustomPayload.Id<ScarredOneResponsePayload>(PAYLOAD_ID)

        val CODEC: PacketCodec<RegistryByteBuf, ScarredOneResponsePayload> = PacketCodec.tuple(
            PacketCodecs.BOOL, ScarredOneResponsePayload::accept,
            Uuids.PACKET_CODEC, ScarredOneResponsePayload::uuid,
            ::ScarredOneResponsePayload
        )
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> = ID
}