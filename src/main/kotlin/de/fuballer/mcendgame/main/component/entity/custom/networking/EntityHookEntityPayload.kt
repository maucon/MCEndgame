package de.fuballer.mcendgame.main.component.entity.custom.networking

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Identifier

class EntityHookEntityPayload(
    val hookerId: Int,
    val hookedId: Int,
    val remove: Boolean,
) : CustomPayload {
    companion object {
        private val ENTITY_HOOK_ENTITY_PACKET_ID: Identifier = IdentifierUtil.default("entity_hook_entity_payload")

        val ID: CustomPayload.Id<EntityHookEntityPayload> =
            CustomPayload.Id<EntityHookEntityPayload>(ENTITY_HOOK_ENTITY_PACKET_ID)

        val CODEC: PacketCodec<RegistryByteBuf, EntityHookEntityPayload> =
            PacketCodec.tuple(
                PacketCodecs.INTEGER, EntityHookEntityPayload::hookerId,
                PacketCodecs.INTEGER, EntityHookEntityPayload::hookedId,
                PacketCodecs.BOOL, EntityHookEntityPayload::remove,
                ::EntityHookEntityPayload
            )
    }

    override fun getId() = ID
}