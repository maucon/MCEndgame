package de.fuballer.mcendgame.client.component.entity.custom.entities.training_dummy

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition


class TrainingDummyEntityModel(
    modelPart: ModelPart,
) : HumanoidModel<TrainingDummyRenderState>(modelPart) {
    companion object {
        val TRAINING_DUMMY = ModelLayerLocation(IdentifierUtil.default("training_dummy"), "main")

        fun getTexturedModelData(): LayerDefinition {
            val modelData = MeshDefinition()
            val modelPartData = modelData.getRoot()
            val head =
                modelPartData.addOrReplaceChild(
                    "head",
                    CubeListBuilder.create().texOffs(13, 0).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, CubeDeformation(0.0f)),
                    PartPose.offset(0.0f, 0.0f, 0.0f)
                )

            val hat = head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0f, 0.0f, 0.0f))

            val body =
                modelPartData.addOrReplaceChild(
                    "body",
                    CubeListBuilder.create().texOffs(17, 17).addBox(-4.0f, 0.0f, -2.0f, 8.0f, 12.0f, 4.0f, CubeDeformation(0.0f)),
                    PartPose.offset(0.0f, 0.0f, 0.0f)
                )

            val left_arm = modelPartData.addOrReplaceChild(
                "left_arm", CubeListBuilder.create().texOffs(42, 13).addBox(-1.0f, -2.0f, -2.0f, 4.0f, 6.0f, 4.0f, CubeDeformation(0.0f))
                    .texOffs(46, 24).addBox(0.0f, 4.0f, -1.0f, 2.0f, 6.0f, 2.0f, CubeDeformation(0.0f))
                    .texOffs(42, 2).addBox(-1.0f, 4.0f, -2.0f, 4.0f, 1.0f, 4.0f, CubeDeformation(0.0f)), PartPose.offset(5.0f, 2.0f, 0.0f)
            )

            val right_arm = modelPartData.addOrReplaceChild(
                "right_arm", CubeListBuilder.create().texOffs(0, 13).addBox(-3.0f, -2.0f, -2.0f, 4.0f, 6.0f, 4.0f, CubeDeformation(0.0f))
                    .texOffs(4, 24).addBox(-2.0f, 4.0f, -1.0f, 2.0f, 6.0f, 2.0f, CubeDeformation(0.0f))
                    .texOffs(0, 2).addBox(-3.0f, 4.0f, -2.0f, 4.0f, 1.0f, 4.0f, CubeDeformation(0.0f)), PartPose.offset(-5.0f, 2.0f, 0.0f)
            )

            val left_leg = modelPartData.addOrReplaceChild(
                "left_leg", CubeListBuilder.create().texOffs(38, 34).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 2.0f, 4.0f, CubeDeformation(0.0f))
                    .texOffs(42, 51).addBox(-1.0f, 2.0f, -1.0f, 2.0f, 9.0f, 2.0f, CubeDeformation(0.0f))
                    .texOffs(38, 41).addBox(-2.0f, 2.0f, -2.0f, 4.0f, 2.0f, 4.0f, CubeDeformation(0.0f)), PartPose.offset(2.0f, 12.0f, 0.0f)
            )

            val right_leg = modelPartData.addOrReplaceChild(
                "right_leg", CubeListBuilder.create().texOffs(4, 34).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 2.0f, 4.0f, CubeDeformation(0.0f))
                    .texOffs(4, 41).addBox(-2.0f, 2.0f, -2.0f, 4.0f, 2.0f, 4.0f, CubeDeformation(0.0f))
                    .texOffs(8, 51).addBox(-1.0f, 2.0f, -1.0f, 2.0f, 9.0f, 2.0f, CubeDeformation(0.0f)), PartPose.offset(-2.0f, 12.0f, 0.0f)
            )

            val base_plate =
                modelPartData.addOrReplaceChild(
                    "base_plate",
                    CubeListBuilder.create().texOffs(5, 51).addBox(-6.0f, -1.0f, -6.0f, 12.0f, 1.0f, 12.0f, CubeDeformation(0.0f)),
                    PartPose.offset(0.0f, 24.0f, 0.0f)
                )
            return LayerDefinition.create(modelData, 64, 64)
        }
    }

    override fun setupAnim(renderState: TrainingDummyRenderState) {
        super.setupAnim(renderState)

        resetPose()

        leftLeg.xRot = -0.02f
        rightLeg.xRot = 0.02f

        leftArm.xRot = -0.2f
        leftArm.yRot = -0.2f

        rightArm.xRot = -0.2f
        rightArm.yRot = 0.2f
    }
}