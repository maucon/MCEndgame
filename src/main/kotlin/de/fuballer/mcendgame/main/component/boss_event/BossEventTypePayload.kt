package de.fuballer.mcendgame.main.component.boss_event

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import io.netty.buffer.ByteBuf
import net.minecraft.core.UUIDUtil
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import java.util.*

private val PAYLOAD_ID = IdentifierUtil.default("boss_event_type")

data class BossEventTypePayload(
    val id: UUID,
    val type: BossEventType,
) : CustomPacketPayload {
    companion object {
        val ID = CustomPacketPayload.Type<BossEventTypePayload>(PAYLOAD_ID)

        private val BOSS_EVENT_TYPE_CODEC: StreamCodec<ByteBuf, BossEventType> = ByteBufCodecs.idMapper({ BossEventType.entries[it] }, { it.ordinal })
        val CODEC: StreamCodec<RegistryFriendlyByteBuf, BossEventTypePayload> = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, BossEventTypePayload::id,
            BOSS_EVENT_TYPE_CODEC, BossEventTypePayload::type,
            ::BossEventTypePayload
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = ID
}