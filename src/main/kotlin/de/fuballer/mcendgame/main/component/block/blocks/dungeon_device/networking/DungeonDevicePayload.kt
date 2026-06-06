package de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.networking

import de.fuballer.mcendgame.main.component.dungeon.level.PlayerDungeonLevel
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.core.BlockPos
import net.minecraft.core.UUIDUtil
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level
import java.util.*

private val PAYLOAD_ID = IdentifierUtil.default("open_dungeon")

data class DungeonDevicePayload(
    val pos: BlockPos,
    val worldKey: ResourceKey<Level>,
    val playerId: UUID,
    val playerDungeonLevel: PlayerDungeonLevel,
) : CustomPacketPayload {
    companion object {
        val ID = CustomPacketPayload.Type<DungeonDevicePayload>(PAYLOAD_ID)

        val CODEC: StreamCodec<RegistryFriendlyByteBuf, DungeonDevicePayload> = StreamCodec.composite(
            BlockPos.STREAM_CODEC, DungeonDevicePayload::pos,
            ResourceKey.streamCodec(Registries.DIMENSION), DungeonDevicePayload::worldKey,
            UUIDUtil.STREAM_CODEC, DungeonDevicePayload::playerId,
            PlayerDungeonLevel.PACKET_CODEC, DungeonDevicePayload::playerDungeonLevel,
            ::DungeonDevicePayload
        )

        val EMPTY = DungeonDevicePayload(
            BlockPos.ZERO,
            ResourceKey.create(
                Registries.DIMENSION,
                IdentifierUtil.default("non_existing")
            ),
            UUID.randomUUID(),
            PlayerDungeonLevel(-1, -1)
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = ID
}