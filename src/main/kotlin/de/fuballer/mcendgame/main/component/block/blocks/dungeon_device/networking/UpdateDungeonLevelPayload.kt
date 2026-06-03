package de.fuballer.mcendgame.main.component.block.blocks.dungeon_device.networking

import de.fuballer.mcendgame.main.component.dungeon.level.PlayerDungeonLevel
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload
import net.minecraft.util.Uuids
import java.util.*

private val PAYLOAD_ID = IdentifierUtil.default("update_dungeon_level")

data class UpdateDungeonLevelPayload(
    val playerId: UUID,
    val dungeonLevel: PlayerDungeonLevel,
) : CustomPayload {
    companion object {
        val ID = CustomPayload.Id<UpdateDungeonLevelPayload>(PAYLOAD_ID)

        val CODEC: PacketCodec<RegistryByteBuf, UpdateDungeonLevelPayload> = PacketCodec.tuple(
            Uuids.PACKET_CODEC, UpdateDungeonLevelPayload::playerId,
            PlayerDungeonLevel.PACKET_CODEC, UpdateDungeonLevelPayload::dungeonLevel,
            ::UpdateDungeonLevelPayload
        )

        val EMPTY = UpdateDungeonLevelPayload(
            UUID.randomUUID(),
            PlayerDungeonLevel(-1, -1)
        )
    }

    override fun getId(): CustomPayload.Id<out CustomPayload> = ID
}