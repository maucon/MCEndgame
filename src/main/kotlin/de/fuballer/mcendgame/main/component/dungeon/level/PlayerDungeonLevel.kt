package de.fuballer.mcendgame.main.component.dungeon.level

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
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
        val PACKET_CODEC: StreamCodec<RegistryFriendlyByteBuf, PlayerDungeonLevel> =
            StreamCodec.composite(
                ByteBufCodecs.VAR_INT, PlayerDungeonLevel::level,
                ByteBufCodecs.VAR_INT, PlayerDungeonLevel::levelProgress,
                ByteBufCodecs.VAR_INT, PlayerDungeonLevel::highestReached,
                ByteBufCodecs.BOOL, PlayerDungeonLevel::locked,
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


        fun write(dungeonLevel: PlayerDungeonLevel, view: ValueOutput) {
            view.store(DUNGEON_LEVEL_NBT, CODEC, dungeonLevel)
        }

        fun read(view: ValueInput): PlayerDungeonLevel = view.read(DUNGEON_LEVEL_NBT, CODEC).orElseGet { PlayerDungeonLevel() }
    }
}