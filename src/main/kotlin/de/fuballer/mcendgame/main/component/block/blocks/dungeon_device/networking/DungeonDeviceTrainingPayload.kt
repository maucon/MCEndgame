package de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.networking

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

private val PAYLOAD_ID = IdentifierUtil.default("open_training_dungeon")

data class DungeonDeviceTrainingPayload(
    val pos: BlockPos,
    val worldKey: ResourceKey<Level>,
    val playerId: UUID,
) : CustomPacketPayload {
    companion object {
        val ID = CustomPacketPayload.Type<DungeonDeviceTrainingPayload>(PAYLOAD_ID)

        val CODEC: StreamCodec<RegistryFriendlyByteBuf, DungeonDeviceTrainingPayload> = StreamCodec.composite(
            BlockPos.STREAM_CODEC, DungeonDeviceTrainingPayload::pos,
            ResourceKey.streamCodec(Registries.DIMENSION), DungeonDeviceTrainingPayload::worldKey,
            UUIDUtil.STREAM_CODEC, DungeonDeviceTrainingPayload::playerId,
            ::DungeonDeviceTrainingPayload
        )

        val EMPTY = DungeonDeviceTrainingPayload(
            BlockPos.ZERO,
            ResourceKey.create(
                Registries.DIMENSION,
                IdentifierUtil.default("non_existing")
            ),
            UUID.randomUUID(),
        )

        fun from(payload: DungeonDevicePayload) = DungeonDeviceTrainingPayload(
            payload.pos,
            payload.worldKey,
            payload.playerId,
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = ID
}