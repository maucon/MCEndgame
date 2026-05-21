package de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.networking

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Uuids
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*

private val PAYLOAD_ID = IdentifierUtil.default("open_training_dungeon")

data class DungeonDeviceTrainingPayload(
    val pos: BlockPos,
    val worldKey: RegistryKey<World>,
    val playerId: UUID,
) : CustomPayload {
    companion object {
        val ID = CustomPayload.Id<DungeonDeviceTrainingPayload>(PAYLOAD_ID)

        val CODEC: PacketCodec<RegistryByteBuf, DungeonDeviceTrainingPayload> = PacketCodec.tuple(
            BlockPos.PACKET_CODEC, DungeonDeviceTrainingPayload::pos,
            RegistryKey.createPacketCodec(RegistryKeys.WORLD), DungeonDeviceTrainingPayload::worldKey,
            Uuids.PACKET_CODEC, DungeonDeviceTrainingPayload::playerId,
            ::DungeonDeviceTrainingPayload
        )

        val EMPTY = DungeonDeviceTrainingPayload(
            BlockPos.ORIGIN,
            RegistryKey.of(
                RegistryKeys.WORLD,
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

    override fun getId(): CustomPayload.Id<out CustomPayload> = ID
}