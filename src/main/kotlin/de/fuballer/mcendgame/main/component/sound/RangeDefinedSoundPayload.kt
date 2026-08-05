package de.fuballer.mcendgame.main.component.sound

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import io.netty.buffer.ByteBuf
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource

private val PAYLOAD_ID = IdentifierUtil.default("range_defined_sound")

data class RangeDefinedSoundPayload(
    val pos: BlockPos,
    val sound: Holder<SoundEvent>,
    val volume: Float,
    val pitch: Float,
    val category: SoundSource,
    val range: Double,
) : CustomPacketPayload {
    companion object {
        val ID = CustomPacketPayload.Type<RangeDefinedSoundPayload>(PAYLOAD_ID)

        private val SOUND_SOURCE_CODEC: StreamCodec<ByteBuf, SoundSource> = ByteBufCodecs.idMapper({ SoundSource.entries[it] }, { it.ordinal })
        val CODEC: StreamCodec<RegistryFriendlyByteBuf, RangeDefinedSoundPayload> = StreamCodec.composite(
            BlockPos.STREAM_CODEC, RangeDefinedSoundPayload::pos,
            SoundEvent.STREAM_CODEC, RangeDefinedSoundPayload::sound,
            ByteBufCodecs.FLOAT, RangeDefinedSoundPayload::volume,
            ByteBufCodecs.FLOAT, RangeDefinedSoundPayload::pitch,
            SOUND_SOURCE_CODEC, RangeDefinedSoundPayload::category,
            ByteBufCodecs.DOUBLE, RangeDefinedSoundPayload::range,
            ::RangeDefinedSoundPayload
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = ID
}