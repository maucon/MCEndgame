package de.fuballer.mcendgame.main.component.entity.custom.networking

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier

class EntityHookEntityPayload(
    val hookerId: Int,
    val hookedId: Int,
    val remove: Boolean,
) : CustomPacketPayload {
    companion object {
        private val ENTITY_HOOK_ENTITY_PACKET_ID: Identifier = IdentifierUtil.default("entity_hook_entity_payload")

        val ID: CustomPacketPayload.Type<EntityHookEntityPayload> =
            CustomPacketPayload.Type<EntityHookEntityPayload>(ENTITY_HOOK_ENTITY_PACKET_ID)

        val CODEC: StreamCodec<RegistryFriendlyByteBuf, EntityHookEntityPayload> =
            StreamCodec.composite(
                ByteBufCodecs.INT, EntityHookEntityPayload::hookerId,
                ByteBufCodecs.INT, EntityHookEntityPayload::hookedId,
                ByteBufCodecs.BOOL, EntityHookEntityPayload::remove,
                ::EntityHookEntityPayload
            )
    }

    override fun type() = ID
}