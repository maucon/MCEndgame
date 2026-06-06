package de.fuballer.mcendgame.main.component.killer.networking

import de.fuballer.mcendgame.main.component.killer.db.KillerEntity
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

private val PAYLOAD_ID = IdentifierUtil.default("killer_entity")

data class KillerEntityPayload(
    val killerEntity: KillerEntity,
) : CustomPacketPayload {
    companion object {
        val ID = CustomPacketPayload.Type<KillerEntityPayload>(PAYLOAD_ID)

        val CODEC: StreamCodec<RegistryFriendlyByteBuf, KillerEntityPayload> = StreamCodec.composite(
            KillerEntity.PACKET_CODEC, KillerEntityPayload::killerEntity,
            ::KillerEntityPayload
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = ID
}