package de.fuballer.mcendgame.client.component.item.custom.armor.model.suede

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

class SuedeLeggingsModel<S : HumanoidRenderState>(
    root: ModelPart
) : HumanoidModel<S>(root) {
    companion object {
        val MODEL_LAYER = ModelLayerLocation(IdentifierUtil.default("suede_leggings"), "main")

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
                    CubeListBuilder.create().texOffs(21, 82).addBox(-4.0f, 9.0f, -2.0f, 8.0f, 3.0f, 4.0f, CubeDeformation(0.05f)),
                    PartPose.offset(0.0f, 0.0f, 0.0f)
                )

            val left_belt = leggings_waist.addOrReplaceChild(
                "left_belt",
                CubeListBuilder.create().texOffs(34, 44).addBox(-5.6f, -0.4f, -2.95f, 6.0f, 3.0f, 6.0f, CubeDeformation(-0.4f)),
                PartPose.offsetAndRotation(4.6f, 8.65f, 0.0f, 0.0f, 0.0f, -0.0873f)
            )

            val right_belt = leggings_waist.addOrReplaceChild(
                "right_belt",
                CubeListBuilder.create().texOffs(8, 44).addBox(-0.4f, 0.1f, -3.05f, 6.0f, 3.0f, 6.0f, CubeDeformation(-0.4f)),
                PartPose.offsetAndRotation(-4.6f, 8.15f, 0.0f, 0.0f, 0.0f, 0.0873f)
            )

            val left_leg = modelPartData.addOrReplaceChild(PartNames.LEFT_LEG, CubeListBuilder.create(), PartPose.offset(2.0f, 12.0f, 0.0f))

            val left_leggings = left_leg.addOrReplaceChild("left_leggings", CubeListBuilder.create(), PartPose.offset(0.0f, 0.0f, 0.0f))

            val left_skirt2 = left_leggings.addOrReplaceChild(
                "left_skirt2",
                CubeListBuilder.create().texOffs(36, 54).addBox(-5.1f, 1.1f, -2.45f, 5.0f, 7.0f, 5.0f, CubeDeformation(-0.1f)),
                PartPose.offsetAndRotation(2.6f, -3.35f, 0.0f, 0.0f, 0.0f, -0.0436f)
            )

            val right_leg = modelPartData.addOrReplaceChild(PartNames.RIGHT_LEG, CubeListBuilder.create(), PartPose.offset(-2.0f, 12.0f, 0.0f))

            val right_leggings = right_leg.addOrReplaceChild("right_leggings", CubeListBuilder.create(), PartPose.offset(0.0f, 0.0f, 0.0f))

            val right_skirt = right_leggings.addOrReplaceChild(
                "right_skirt",
                CubeListBuilder.create().texOffs(10, 54).addBox(0.1f, 1.6f, -2.55f, 5.0f, 7.0f, 5.0f, CubeDeformation(-0.1f)),
                PartPose.offsetAndRotation(-2.6f, -3.85f, 0.0f, 0.0f, 0.0f, 0.0436f)
            )
            return LayerDefinition.create(modelData, 128, 128)
        }
    }

    override fun setupAnim(renderState: S) {}
}