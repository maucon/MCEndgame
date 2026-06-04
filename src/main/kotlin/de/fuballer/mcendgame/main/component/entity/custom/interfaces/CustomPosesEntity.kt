package de.fuballer.mcendgame.main.component.entity.custom.interfaces

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import io.netty.buffer.ByteBuf
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityDataRegistry
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.syncher.EntityDataSerializer

interface CustomPosesEntity {
    fun setPose(pose: CustomPose)

    companion object {
        val CUSTOM_POSE_TDH: EntityDataSerializer<CustomPose> = EntityDataSerializer.forValueType(CustomPose.PACKET_CODEC)
            .also { FabricEntityDataRegistry.register(IdentifierUtil.default("custom_pose_tracked_data"), it) }
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
            val PACKET_CODEC: StreamCodec<ByteBuf, CustomPose> = ByteBufCodecs.idMapper(
                { index: Int -> entries[index] },
                { value: CustomPose -> value.ordinal })
        }
    }
}