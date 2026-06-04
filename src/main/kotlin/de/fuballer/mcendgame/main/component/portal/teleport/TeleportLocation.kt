package de.fuballer.mcendgame.main.component.portal.teleport

import com.mojang.logging.LogUtils
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.codecs.RecordCodecBuilder
import de.fuballer.mcendgame.main.configuration.RuntimeConfig
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.phys.Vec3

data class TeleportLocation(
    val world: ServerLevel,
    val coordinates: Vec3,
    val xRot: Float = 0.0F,
    val yRot: Float = 0.0F
) {
    companion object {
        private val log = LogUtils.getLogger()

        private val WORLD_CODEC: Codec<ServerLevel> = Identifier.CODEC.comapFlatMap(
            { id ->
                val worldKey = ResourceKey.create(Registries.DIMENSION, id)
                val world = RuntimeConfig.SERVER.getLevel(worldKey)
                if (world != null) {
                    DataResult.success(world)
                } else {
                    log.warn("World '{}' not found, skipping teleport location", worldKey.identifier())
                    DataResult.error { "World with key '$worldKey' not found" }
                }
            },
            { world -> world.dimension().identifier() }
        )

        val CODEC: Codec<TeleportLocation> = RecordCodecBuilder.create { instance ->
            instance.group(
                WORLD_CODEC
                    .fieldOf("World")
                    .forGetter { it.world },

                Vec3.CODEC
                    .fieldOf("Coordinates")
                    .forGetter { location -> location.coordinates },

                Codec.FLOAT
                    .optionalFieldOf("RotationX", 0.0f)
                    .forGetter { location -> location.xRot },

                Codec.FLOAT
                    .optionalFieldOf("RotationY", 0.0f)
                    .forGetter { location -> location.yRot }

            ).apply(instance, ::TeleportLocation)
        }
    }
}