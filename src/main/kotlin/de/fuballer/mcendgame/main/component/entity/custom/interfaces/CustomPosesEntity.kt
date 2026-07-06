package de.fuballer.mcendgame.main.component.entity.custom.interfaces

import io.netty.buffer.ByteBuf
import net.minecraft.entity.data.TrackedDataHandler
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs

interface CustomPosesEntity {
    fun setPose(pose: CustomPose)

    companion object {
        val CUSTOM_POSE_TDH: TrackedDataHandler<CustomPose> = TrackedDataHandler.create(CustomPose.PACKET_CODEC)
            .also { TrackedDataHandlerRegistry.register(it) }
    }

    enum class CustomPose {
        IDLING,
        WALKING,
        WALKING_BW,
        WALKING_RIGHT,
        WALKING_LEFT,
        SLAMMING,
        MELEE_ATTACKING,
        SPITTING;

        companion object {
            val PACKET_CODEC: PacketCodec<ByteBuf, CustomPose> = PacketCodecs.indexed(
                { index: Int -> entries[index] },
                { value: CustomPose -> value.ordinal })
        }
    }
}