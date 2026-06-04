package de.fuballer.mcendgame.client.component.item.custom.armor.model.iceborne

import de.fuballer.mcendgame.client.component.item.custom.ModelPartDataExtension.createEmptyChild
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartNames
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.renderer.entity.state.HumanoidRenderState

class IceborneModel<S : HumanoidRenderState>(
    root: ModelPart
) : HumanoidModel<S>(root) {
    companion object {
        val MODEL_LAYER = ModelLayerLocation(IdentifierUtil.default("iceborne"), "main")

        fun getTexturedModelData(): LayerDefinition {
            val modelData = MeshDefinition()
            val modelPartData = modelData.root

            val head = modelPartData.createEmptyChild(PartNames.HEAD)
            val hat = head.createEmptyChild(PartNames.HAT)
            val body = modelPartData.createEmptyChild(PartNames.BODY)
            val right_arm = modelPartData.createEmptyChild(PartNames.RIGHT_ARM)
            val left_arm = modelPartData.createEmptyChild(PartNames.LEFT_ARM)
            val right_leg = modelPartData.createEmptyChild(PartNames.RIGHT_LEG)
            val left_leg = modelPartData.createEmptyChild(PartNames.LEFT_LEG)

            val helmet = head.addOrReplaceChild(
                "helmet",
                CubeListBuilder.create().texOffs(16, 48).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, CubeDeformation(1.0f))
                    .texOffs(18, 31).addBox(-1.0f, -9.75f, -6.0f, 2.0f, 4.0f, 12.0f, CubeDeformation(0.0f))
                    .texOffs(22, 20).addBox(-1.0f, -11.2f, -4.0f, 2.0f, 2.0f, 8.0f, CubeDeformation(-0.25f))
                    .texOffs(29, 15).addBox(-1.0f, -6.25f, -5.9f, 2.0f, 3.0f, 1.0f, CubeDeformation(-0.1f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val left_horn = helmet.addOrReplaceChild(
                "left_horn",
                CubeListBuilder.create().texOffs(44, 0).addBox(-2.0f, -3.0f, -3.0f, 4.0f, 6.0f, 6.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(4.5f, -6.25f, 1.0f, 0.0f, 0.0f, -0.1309f)
            )

            val left_horn_0 = left_horn.addOrReplaceChild(
                "left_horn_0",
                CubeListBuilder.create().texOffs(46, 13).addBox(-1.25f, -2.0f, -2.0f, 4.0f, 4.0f, 4.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(2.0f, 0.0f, 0.0f, 0.0f, 0.0873f, -0.0873f)
            )

            val left_horn_1 = left_horn_0.addOrReplaceChild(
                "left_horn_1",
                CubeListBuilder.create().texOffs(47, 22).addBox(-1.5f, -1.5f, -1.5f, 4.0f, 3.0f, 3.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(2.75f, -0.2f, -0.1f, -0.0669f, 0.3431f, -0.5344f)
            )

            val left_horn_2 = left_horn_1.addOrReplaceChild(
                "left_horn_2",
                CubeListBuilder.create().texOffs(47, 29).addBox(-1.05f, -1.25f, -1.5f, 4.0f, 3.0f, 3.0f, CubeDeformation(-0.125f)),
                PartPose.offsetAndRotation(2.25f, -0.5f, -0.25f, 0.0f, 0.2618f, -0.6109f)
            )

            val left_horn_3 = left_horn_2.addOrReplaceChild(
                "left_horn_3",
                CubeListBuilder.create().texOffs(49, 36).addBox(-0.6f, -1.05f, -1.1f, 3.0f, 2.0f, 2.0f, CubeDeformation(0.1f)),
                PartPose.offsetAndRotation(2.8f, 0.25f, 0.0f, -0.0832f, -0.1882f, -0.4456f)
            )

            val left_horn_4 = left_horn_3.addOrReplaceChild(
                "left_horn_4",
                CubeListBuilder.create().texOffs(49, 41).addBox(-1.0f, -1.1f, -1.0f, 3.0f, 2.0f, 2.0f, CubeDeformation(-0.15f)),
                PartPose.offsetAndRotation(2.4f, 0.0f, -0.1f, 0.0f, 0.0f, -0.3491f)
            )

            val left_horn_5 = left_horn_4.addOrReplaceChild(
                "left_horn_5",
                CubeListBuilder.create().texOffs(49, 46).addBox(-1.0f, -0.9f, -1.0f, 3.0f, 2.0f, 2.0f, CubeDeformation(-0.35f)),
                PartPose.offsetAndRotation(1.8f, -0.2f, 0.0f, -0.0873f, -0.0873f, 0.1745f)
            )

            val left_horn_6 = left_horn_5.addOrReplaceChild(
                "left_horn_6",
                CubeListBuilder.create().texOffs(51, 51).addBox(-0.2f, -0.4f, -0.5f, 2.0f, 1.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(1.6f, 0.1f, 0.0f, -0.3054f, -0.1745f, 0.2618f)
            )

            val right_horn = helmet.addOrReplaceChild(
                "right_horn",
                CubeListBuilder.create().texOffs(0, 0).addBox(-2.0f, -3.0f, -3.0f, 4.0f, 6.0f, 6.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(-4.5f, -6.25f, 1.0f, 0.0f, 0.0f, 0.1309f)
            )

            val right_horn_0 = right_horn.addOrReplaceChild(
                "right_horn_0",
                CubeListBuilder.create().texOffs(2, 13).addBox(-2.75f, -2.0f, -2.0f, 4.0f, 4.0f, 4.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(-2.0f, 0.0f, 0.0f, 0.0f, -0.0873f, 0.0873f)
            )

            val right_horn_1 = right_horn_0.addOrReplaceChild(
                "right_horn_1",
                CubeListBuilder.create().texOffs(3, 22).addBox(-2.5f, -1.5f, -1.5f, 4.0f, 3.0f, 3.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(-2.75f, -0.2f, -0.1f, -0.0669f, -0.3431f, 0.5344f)
            )

            val right_horn_2 = right_horn_1.addOrReplaceChild(
                "right_horn_2",
                CubeListBuilder.create().texOffs(3, 29).addBox(-2.95f, -1.25f, -1.5f, 4.0f, 3.0f, 3.0f, CubeDeformation(-0.125f)),
                PartPose.offsetAndRotation(-2.25f, -0.5f, -0.25f, 0.0f, -0.2618f, 0.6109f)
            )

            val right_horn_3 = right_horn_2.addOrReplaceChild(
                "right_horn_3",
                CubeListBuilder.create().texOffs(5, 36).addBox(-2.4f, -1.05f, -1.1f, 3.0f, 2.0f, 2.0f, CubeDeformation(0.1f)),
                PartPose.offsetAndRotation(-2.8f, 0.25f, 0.0f, -0.0832f, 0.1882f, 0.4456f)
            )

            val right_horn_4 = right_horn_3.addOrReplaceChild(
                "right_horn_4",
                CubeListBuilder.create().texOffs(5, 41).addBox(-2.0f, -1.1f, -1.0f, 3.0f, 2.0f, 2.0f, CubeDeformation(-0.15f)),
                PartPose.offsetAndRotation(-2.4f, 0.0f, -0.1f, 0.0f, 0.0f, 0.3491f)
            )

            val right_horn_5 = right_horn_4.addOrReplaceChild(
                "right_horn_5",
                CubeListBuilder.create().texOffs(5, 46).addBox(-2.0f, -0.9f, -1.0f, 3.0f, 2.0f, 2.0f, CubeDeformation(-0.35f)),
                PartPose.offsetAndRotation(-1.8f, -0.2f, 0.0f, -0.0873f, 0.0873f, -0.1745f)
            )

            val right_horn_6 = right_horn_5.addOrReplaceChild(
                "right_horn_6",
                CubeListBuilder.create().texOffs(7, 51).addBox(-1.8f, -0.4f, -0.5f, 2.0f, 1.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(-1.6f, 0.1f, 0.0f, -0.3054f, 0.1745f, -0.2618f)
            )
            return LayerDefinition.create(modelData, 64, 64)
        }
    }

    override fun setupAnim(renderState: S) {}
}