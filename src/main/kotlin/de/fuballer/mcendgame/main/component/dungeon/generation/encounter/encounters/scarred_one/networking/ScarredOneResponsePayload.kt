package de.fuballer.mcendgame.main.component.dungeon.generation.encounter.encounters.scarred_one.networking

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.core.UUIDUtil
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import java.util.*

private val PAYLOAD_ID = IdentifierUtil.default("scarred_one_response")

data class ScarredOneResponsePayload(
    val accept: Boolean,
    val uuid: UUID,
) : CustomPacketPayload {
    companion object {
        val ID = CustomPacketPayload.Type<ScarredOneResponsePayload>(PAYLOAD_ID)

        val CODEC: StreamCodec<RegistryFriendlyByteBuf, ScarredOneResponsePayload> = StreamCodec.composite(
            ByteBufCodecs.BOOL, ScarredOneResponsePayload::accept,
            UUIDUtil.STREAM_CODEC, ScarredOneResponsePayload::uuid,
            ::ScarredOneResponsePayload
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = ID
}