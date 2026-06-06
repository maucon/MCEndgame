package de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.networking

import de.fuballer.mcendgame.main.component.dungeon.level.PlayerDungeonLevel
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.core.UUIDUtil
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import java.util.*

private val PAYLOAD_ID = IdentifierUtil.default("update_dungeon_level")

data class UpdateDungeonLevelPayload(
    val playerId: UUID,
    val dungeonLevel: PlayerDungeonLevel,
) : CustomPacketPayload {
    companion object {
        val ID = CustomPacketPayload.Type<UpdateDungeonLevelPayload>(PAYLOAD_ID)

        val CODEC: StreamCodec<RegistryFriendlyByteBuf, UpdateDungeonLevelPayload> = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC, UpdateDungeonLevelPayload::playerId,
            PlayerDungeonLevel.PACKET_CODEC, UpdateDungeonLevelPayload::dungeonLevel,
            ::UpdateDungeonLevelPayload
        )

        val EMPTY = UpdateDungeonLevelPayload(
            UUID.randomUUID(),
            PlayerDungeonLevel(-1, -1)
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = ID
}