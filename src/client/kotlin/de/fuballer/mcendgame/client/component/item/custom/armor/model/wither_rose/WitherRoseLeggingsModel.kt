package de.fuballer.mcendgame.client.component.item.custom.armor.model.wither_rose

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

class WitherRoseLeggingsModel<S : HumanoidRenderState>(
    root: ModelPart,
) : HumanoidModel<S>(root) {
    companion object {
        val MODEL_LAYER = ModelLayerLocation(IdentifierUtil.default("wither_rose_leggings"), "main")

        fun getTexturedModelData(): LayerDefinition {
            val modelData = MeshDefinition()
            val modelPartData = modelData.root

            val head = modelPartData.createEmptyChild(PartNames.HEAD)
            val hat = head.createEmptyChild(PartNames.HAT)
            val left_arm = modelPartData.createEmptyChild(PartNames.LEFT_ARM)
            val right_arm = modelPartData.createEmptyChild(PartNames.RIGHT_ARM)

            val body = modelPartData.addOrReplaceChild(PartNames.BODY, CubeListBuilder.create(), PartPose.offset(0.0f, 0.0f, 0.0f))

            val leggings_waist =
                body.addOrReplaceChild(
                    "leggings_waist",
                    CubeListBuilder.create().texOffs(9, 92).addBox(-4.0f, 8.0f, -2.0f, 8.0f, 4.0f, 4.0f, CubeDeformation(0.05f)),
                    PartPose.offset(0.0f, 0.0f, 0.0f)
                )

            val skirt = leggings_waist.addOrReplaceChild("skirt", CubeListBuilder.create(), PartPose.offset(0.0f, 0.25f, 0.0f))

            val skirtBackRight = skirt.addOrReplaceChild(
                "skirtBackRight",
                CubeListBuilder.create().texOffs(0, 79).addBox(0.0f, 0.0f, 0.0f, 4.0f, 12.0f, 0.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(-4.25f, 8.0f, 3.1f, 0.0872f, -0.0038f, 0.0435f)
            )

            val skirtBackLeft = skirt.addOrReplaceChild(
                "skirtBackLeft",
                CubeListBuilder.create().texOffs(43, 79).addBox(-4.0f, 0.0f, 0.0f, 4.0f, 12.0f, 0.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(4.25f, 8.0f, 3.1f, 0.0872f, 0.0038f, -0.0435f)
            )

            val skirtLeft = skirt.addOrReplaceChild(
                "skirtLeft",
                CubeListBuilder.create().texOffs(34, 75).addBox(0.0f, 0.0f, 0.0f, 0.0f, 12.0f, 4.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(5.1f, 8.0f, -2.0f, 0.0436f, 0.0f, -0.0436f)
            )

            val skirtRight = skirt.addOrReplaceChild(
                "skirtRight",
                CubeListBuilder.create().texOffs(9, 75).addBox(0.0f, 0.0f, 0.0f, 0.0f, 12.0f, 4.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(-5.1f, 8.0f, -2.0f, 0.0436f, 0.0f, 0.0436f)
            )

            val skirtFrontRight = skirt.addOrReplaceChild(
                "skirtFrontRight",
                CubeListBuilder.create().texOffs(18, 79).addBox(0.0f, 0.0f, 0.0f, 2.0f, 12.0f, 0.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(-4.25f, 8.0f, -3.1f, -0.0436f, 0.0019f, 0.0436f)
            )

            val skirtFrontLeft = skirt.addOrReplaceChild(
                "skirtFrontLeft",
                CubeListBuilder.create().texOffs(23, 86).addBox(-5.0f, 0.0f, 0.0f, 5.0f, 5.0f, 0.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(4.25f, 8.0f, -3.1f, -0.0436f, -0.0019f, -0.0436f)
            )

            val beltLeft = skirt.addOrReplaceChild(
                "beltLeft",
                CubeListBuilder.create().texOffs(25, 66).addBox(-6.75f, -0.25f, -3.45f, 7.0f, 3.0f, 7.0f, CubeDeformation(-0.25f)),
                PartPose.offsetAndRotation(5.25f, 7.25f, 0.0f, 0.0f, 0.0f, -0.0873f)
            )

            val beltRight = skirt.addOrReplaceChild(
                "beltRight",
                CubeListBuilder.create().texOffs(0, 68).addBox(-0.25f, -0.25f, -3.5f, 5.0f, 3.0f, 7.0f, CubeDeformation(-0.25f)),
                PartPose.offsetAndRotation(-5.25f, 7.25f, 0.0f, 0.0f, 0.0f, 0.1309f)
            )

            val beltFront = skirt.addOrReplaceChild(
                "beltFront",
                CubeListBuilder.create().texOffs(24, 76).addBox(-1.5f, -0.5f, -0.5f, 3.0f, 8.0f, 1.0f, CubeDeformation(-0.5f)),
                PartPose.offsetAndRotation(-1.0f, 7.6f, -3.35f, -0.0436f, 0.0019f, 0.0436f)
            )

            val left_leg = modelPartData.addOrReplaceChild(PartNames.LEFT_LEG, CubeListBuilder.create(), PartPose.offset(2.0f, 12.0f, 0.0f))

            val left_leggings = left_leg.addOrReplaceChild(
                "left_leggings",
                CubeListBuilder.create().texOffs(22, 101).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 10.0f, 4.0f, CubeDeformation(0.05f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val right_leg = modelPartData.addOrReplaceChild(PartNames.RIGHT_LEG, CubeListBuilder.create(), PartPose.offset(-2.0f, 12.0f, 0.0f))

            val right_leggings = right_leg.addOrReplaceChild(
                "right_leggings",
                CubeListBuilder.create().texOffs(4, 101).addBox(-2.0f, 0.0f, -2.0f, 4.0f, 10.0f, 4.0f, CubeDeformation(0.05f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )
            return LayerDefinition.create(modelData, 128, 128)
        }
    }

    override fun setupAnim(renderState: S) {}
}