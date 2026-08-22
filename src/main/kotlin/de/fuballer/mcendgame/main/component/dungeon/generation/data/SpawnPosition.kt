package de.fuballer.mcendgame.main.component.dungeon.generation.data

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import de.fuballer.mcendgame.main.util.extension.Vec3iExtension.toBlockPos
import net.minecraft.core.Vec3i
import net.minecraft.world.entity.Entity

data class SpawnPosition(
    val pos: Vec3i,
    val rot: Double = 0.0,
) {
    fun blockPos() = pos.toBlockPos()

    companion object {
        fun at(entity: Entity) = SpawnPosition(entity.blockPosition(), 0.0)

        val CODEC: Codec<SpawnPosition> = RecordCodecBuilder.create { instance ->
            instance.group(
                Vec3i.CODEC
                    .fieldOf("Position")
                    .forGetter { position -> position.pos },

                Codec.DOUBLE
                    .optionalFieldOf("Rotation", 0.0)
                    .forGetter { position -> position.rot },

                ).apply(instance, ::SpawnPosition)
        }
    }
}