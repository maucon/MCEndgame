package de.fuballer.mcendgame.main.component.dungeon.level

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtOps
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import kotlin.math.max

private const val DUNGEON_LEVEL_NBT = "PlayerDungeonLevel"

data class PlayerDungeonLevel(
    var level: Int = 1,
    var levelProgress: Int = 0,
    var highestReached: Int = level,
    var locked: Boolean = false,
) {
    init {
        highestReached = max(level, highestReached)
    }

    companion object {
        val PACKET_CODEC: PacketCodec<RegistryByteBuf, PlayerDungeonLevel> =
            PacketCodec.tuple(
                PacketCodecs.VAR_INT, PlayerDungeonLevel::level,
                PacketCodecs.VAR_INT, PlayerDungeonLevel::levelProgress,
                PacketCodecs.VAR_INT, PlayerDungeonLevel::highestReached,
                PacketCodecs.BOOL, PlayerDungeonLevel::locked,
                ::PlayerDungeonLevel
            )

        val CODEC: Codec<PlayerDungeonLevel> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.INT.fieldOf("level").forGetter(PlayerDungeonLevel::level),
                Codec.INT.fieldOf("level_progress").forGetter(PlayerDungeonLevel::levelProgress),
                Codec.INT.optionalFieldOf("highest_reached", 1).forGetter(PlayerDungeonLevel::highestReached),
                Codec.BOOL.optionalFieldOf("locked", false).forGetter(PlayerDungeonLevel::locked),
            ).apply(instance) { level, progress, highest, locked ->
                PlayerDungeonLevel(level, progress, max(level, highest), locked)
            }
        }


        fun write(dungeonLevel: PlayerDungeonLevel, nbt: NbtCompound) {
            nbt.put(DUNGEON_LEVEL_NBT, CODEC.encodeStart(NbtOps.INSTANCE, dungeonLevel).result().get())
        }

        fun read(nbt: NbtCompound): PlayerDungeonLevel =
            CODEC.parse(NbtOps.INSTANCE, nbt.get(DUNGEON_LEVEL_NBT)).result().orElseGet { PlayerDungeonLevel() }
    }
}