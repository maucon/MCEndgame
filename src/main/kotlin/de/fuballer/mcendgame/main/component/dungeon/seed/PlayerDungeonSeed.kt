package de.fuballer.mcendgame.main.component.dungeon.seed

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import de.fuballer.mcendgame.main.component.dungeon.type.DungeonType
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtOps

private const val DUNGEON_SEED_NBT = "PlayerDungeonSeed"

data class PlayerDungeonSeed(
    var seed: Long,
    var type: DungeonType,
    var hasBeenUsed: Boolean = false,
) {
    companion object {
        val CODEC: Codec<PlayerDungeonSeed> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.LONG.fieldOf("seed").forGetter(PlayerDungeonSeed::seed),
                Codec.STRING.fieldOf("type").forGetter { it.type.name },
                Codec.BOOL.optionalFieldOf("hasBeenUsed", false).forGetter { it.hasBeenUsed },
            ).apply(instance) { seed, typeName, hasBeenUsed ->
                val type = runCatching { DungeonType.valueOf(typeName) }.getOrDefault(DungeonType.STRONGHOLD)
                PlayerDungeonSeed(seed, type, hasBeenUsed)
            }
        }

        fun write(seed: PlayerDungeonSeed, nbt: NbtCompound) {
            nbt.put(DUNGEON_SEED_NBT, CODEC.encodeStart(NbtOps.INSTANCE, seed).result().get())
        }

        fun read(nbt: NbtCompound): PlayerDungeonSeed? =
            CODEC.parse(NbtOps.INSTANCE, nbt.get(DUNGEON_SEED_NBT)).result().orElse(null)
    }
}