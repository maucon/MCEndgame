package de.fuballer.mcendgame.client.component.entity.custom.entities.training_dummy

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.model.*
import net.minecraft.client.render.entity.model.BipedEntityModel
import net.minecraft.client.render.entity.model.EntityModelLayer


class TrainingDummyEntityModel(
    modelPart: ModelPart,
) : BipedEntityModel<TrainingDummyRenderState>(modelPart) {
    companion object {
        val TRAINING_DUMMY = EntityModelLayer(IdentifierUtil.default("training_dummy"), "main")

        fun getTexturedModelData(): TexturedModelData {
            val modelData = ModelData()
            val modelPartData = modelData.getRoot()
            val head =
                modelPartData.addChild("head", ModelPartBuilder.create().uv(13, 0).cuboid(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, Dilation(0.0f)), ModelTransform.origin(0.0f, 0.0f, 0.0f))

            val hat = head.addChild("hat", ModelPartBuilder.create(), ModelTransform.origin(0.0f, 0.0f, 0.0f))

            val body =
                modelPartData.addChild("body", ModelPartBuilder.create().uv(17, 17).cuboid(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, Dilation(0.0f)), ModelTransform.origin(0.0f, 0.0f, 0.0f))

            val left_arm = modelPartData.addChild(
                "left_arm", ModelPartBuilder.create().uv(42, 13).cuboid(-1.0f, -2.0f, -2.0f, 4.0f, 6.0f, 4.0f, Dilation(0.0f))
                    .uv(46, 24).cuboid(0.0f, 4.0f, -1.0f, 2.0f, 6.0f, 2.0f, Dilation(0.0f))
                    .uv(42, 2).cuboid(-1.0f, 4.0f, -2.0f, 4.0f, 1.0f, 4.0f, Dilation(0.0f)), ModelTransform.origin(5.0f, 2.0f, 0.0f)
            )

            val right_arm = modelPartData.addChild(
                "right_arm", ModelPartBuilder.create().uv(0, 13).cuboid(-3.0f, -2.0f, -2.0f, 4.0f, 6.0f, 4.0f, Dilation(0.0f))
                    .uv(4, 24).cuboid(-2.0f, 4.0f, -1.0f, 2.0f, 6.0f, 2.0f, Dilation(0.0f))
                    .uv(0, 2).cuboid(-3.0f, 4.0f, -2.0f, 4.0f, 1.0f, 4.0f, Dilation(0.0f)), ModelTransform.origin(-5.0f, 2.0f, 0.0f)
            )

            val left_leg = modelPartData.addChild(
                "left_leg", ModelPartBuilder.create().uv(38, 34).cuboid(-2.0f, 0.0f, -2.0f, 4.0f, 2.0f, 4.0f, Dilation(0.0f))
                    .uv(42, 51).cuboid(-1.0f, 2.0f, -1.0f, 2.0f, 9.0f, 2.0f, Dilation(0.0f))
                    .uv(38, 41).cuboid(-2.0f, 2.0f, -2.0f, 4.0f, 2.0f, 4.0f, Dilation(0.0f)), ModelTransform.origin(2.0f, 12.0f, 0.0f)
            )

            val right_leg = modelPartData.addChild(
                "right_leg", ModelPartBuilder.create().uv(4, 34).cuboid(-2.0f, 0.0f, -2.0f, 4.0f, 2.0f, 4.0f, Dilation(0.0f))
                    .uv(4, 41).cuboid(-2.0f, 2.0f, -2.0f, 4.0f, 2.0f, 4.0f, Dilation(0.0f))
                    .uv(8, 51).cuboid(-1.0f, 2.0f, -1.0f, 2.0f, 9.0f, 2.0f, Dilation(0.0f)), ModelTransform.origin(-2.0f, 12.0f, 0.0f)
            )

            val base_plate =
                modelPartData.addChild(
                    "base_plate",
                    ModelPartBuilder.create().uv(5, 51).cuboid(-6.0f, -1.0f, -6.0f, 12.0f, 1.0f, 12.0f, Dilation(0.0f)),
                    ModelTransform.origin(0.0f, 24.0f, 0.0f)
                )
            return TexturedModelData.of(modelData, 64, 64)
        }
    }

    override fun setAngles(renderState: TrainingDummyRenderState) {
        super.setAngles(renderState)

        resetTransforms()

        leftLeg.pitch = -0.02f
        rightLeg.pitch = 0.02f

        leftArm.pitch = -0.2f
        leftArm.yaw = -0.2f

        rightArm.pitch = -0.2f
        rightArm.yaw = 0.2f
    }
}