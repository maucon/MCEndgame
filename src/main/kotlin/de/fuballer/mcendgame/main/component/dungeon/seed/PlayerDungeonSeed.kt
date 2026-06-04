package de.fuballer.mcendgame.main.component.dungeon.seed

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import de.fuballer.mcendgame.main.component.dungeon.type.DungeonType
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import kotlin.jvm.optionals.getOrNull

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

        fun write(seed: PlayerDungeonSeed, view: ValueOutput) {
            view.store(DUNGEON_SEED_NBT, CODEC, seed)
        }

        fun read(view: ValueInput): PlayerDungeonSeed? = view.read(DUNGEON_SEED_NBT, CODEC).getOrNull()
    }
}