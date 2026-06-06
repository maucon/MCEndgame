package de.fuballer.mcendgame.main.component.dungeon.generation.encounter.encounters.scarred_one.networking

import de.fuballer.mcendgame.main.component.dungeon.generation.encounter.encounters.scarred_one.data.RolledScarredOneEffect
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.core.UUIDUtil
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import java.util.*

private val PAYLOAD_ID = IdentifierUtil.default("scarred_one_effects")

data class ScarredOneEffectsPayload(
    val positiveEffects: List<RolledScarredOneEffect>,
    val negativeEffects: List<RolledScarredOneEffect>,
    val uuid: UUID,
) : CustomPacketPayload {
    companion object {
        val ID = CustomPacketPayload.Type<ScarredOneEffectsPayload>(PAYLOAD_ID)

        val CODEC: StreamCodec<RegistryFriendlyByteBuf, ScarredOneEffectsPayload> = StreamCodec.composite(
            ByteBufCodecs.collection(::ArrayList, RolledScarredOneEffect.PACKET_CODEC), ScarredOneEffectsPayload::positiveEffects,
            ByteBufCodecs.collection(::ArrayList, RolledScarredOneEffect.PACKET_CODEC), ScarredOneEffectsPayload::negativeEffects,
            UUIDUtil.STREAM_CODEC, ScarredOneEffectsPayload::uuid,
            ::ScarredOneEffectsPayload
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = ID
}