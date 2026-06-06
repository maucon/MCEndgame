package de.fuballer.mcendgame.client.component.item.custom.armor.model.druids

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

class DruidsHelmetModel<S : HumanoidRenderState>(
    root: ModelPart
) : HumanoidModel<S>(root) {
    companion object {
        val MODEL_LAYER = ModelLayerLocation(IdentifierUtil.default("druids_helmet"), "main")

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
                CubeListBuilder.create().texOffs(38, 73).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f, CubeDeformation(0.6f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val crystal = helmet.addOrReplaceChild(
                "crystal",
                CubeListBuilder.create().texOffs(36, 76).addBox(-3.0f, -3.0f, -2.0f, 3.0f, 3.0f, 2.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, -5.5f, -3.0f, 0.0f, 0.0f, 0.7854f)
            )

            val helmet_horn_left_base = helmet.addOrReplaceChild(
                "helmet_horn_left_base",
                CubeListBuilder.create().texOffs(70, 83).addBox(0.0f, -3.0f, -1.5f, 1.0f, 3.0f, 3.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(4.5f, -4.75f, 2.0f, 0.2182f, 0.0f, -0.0873f)
            )

            val helmet_horn_left_0 = helmet_horn_left_base.addOrReplaceChild(
                "helmet_horn_left_0",
                CubeListBuilder.create().texOffs(78, 85).addBox(0.0f, -2.0f, -1.0f, 2.0f, 2.0f, 2.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(1.0f, -0.5f, 0.0f, 0.0f, 0.0f, -0.4363f)
            )

            val helmet_horn_left_1 = helmet_horn_left_0.addOrReplaceChild(
                "helmet_horn_left_1",
                CubeListBuilder.create().texOffs(86, 85).addBox(-0.25f, -1.75f, -1.0f, 2.0f, 2.0f, 2.0f, CubeDeformation(-0.25f)),
                PartPose.offsetAndRotation(2.0f, -0.25f, 0.0f, 0.0f, 0.0f, -0.4363f)
            )

            val helmet_horn_left_2 = helmet_horn_left_1.addOrReplaceChild(
                "helmet_horn_left_2",
                CubeListBuilder.create().texOffs(94, 85).addBox(-0.5f, -1.5f, -1.0f, 2.0f, 2.0f, 2.0f, CubeDeformation(-0.5f)),
                PartPose.offsetAndRotation(1.5f, -0.25f, 0.0f, 0.0f, 0.0f, -0.4363f)
            )

            val helmet_horn_left_3 = helmet_horn_left_2.addOrReplaceChild(
                "helmet_horn_left_3",
                CubeListBuilder.create().texOffs(102, 87).addBox(-0.5f, -0.75f, -0.5f, 2.0f, 1.0f, 1.0f, CubeDeformation(-0.25f)),
                PartPose.offsetAndRotation(0.5f, -0.15f, 0.0f, 0.0f, 0.0f, -0.1309f)
            )

            val helmet_horn_right_base = helmet.addOrReplaceChild(
                "helmet_horn_right_base",
                CubeListBuilder.create().texOffs(30, 83).addBox(-1.0f, -3.0f, -1.5f, 1.0f, 3.0f, 3.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(-4.5f, -4.75f, 2.0f, 0.2182f, 0.0f, 0.0873f)
            )

            val helmet_horn_right_0 = helmet_horn_right_base.addOrReplaceChild(
                "helmet_horn_right_0",
                CubeListBuilder.create().texOffs(22, 85).addBox(-2.0f, -2.0f, -1.0f, 2.0f, 2.0f, 2.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(-1.0f, -0.5f, 0.0f, 0.0f, 0.0f, 0.4363f)
            )

            val helmet_horn_right_1 = helmet_horn_right_0.addOrReplaceChild(
                "helmet_horn_right_1",
                CubeListBuilder.create().texOffs(14, 85).addBox(-1.75f, -1.75f, -1.0f, 2.0f, 2.0f, 2.0f, CubeDeformation(-0.25f)),
                PartPose.offsetAndRotation(-2.0f, -0.25f, 0.0f, 0.0f, 0.0f, 0.4363f)
            )

            val helmet_horn_right_2 = helmet_horn_right_1.addOrReplaceChild(
                "helmet_horn_right_2",
                CubeListBuilder.create().texOffs(6, 85).addBox(-1.5f, -1.5f, -1.0f, 2.0f, 2.0f, 2.0f, CubeDeformation(-0.5f)),
                PartPose.offsetAndRotation(-1.5f, -0.25f, 0.0f, 0.0f, 0.0f, 0.4363f)
            )

            val helmet_horn_right_3 = helmet_horn_right_2.addOrReplaceChild(
                "helmet_horn_right_3",
                CubeListBuilder.create().texOffs(0, 87).addBox(-1.5f, -0.75f, -0.5f, 2.0f, 1.0f, 1.0f, CubeDeformation(-0.25f)),
                PartPose.offsetAndRotation(-0.5f, -0.15f, 0.0f, 0.0f, 0.0f, 0.1309f)
            )
            return LayerDefinition.create(modelData, 128, 128)
        }
    }

    override fun setupAnim(renderState: S) {}
}