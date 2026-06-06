package de.fuballer.mcendgame.client.component.item.custom.armor.model.emberreign

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

class EmberreignModel<S : HumanoidRenderState>(
    root: ModelPart
) : HumanoidModel<S>(root) {
    companion object {
        val MODEL_LAYER = ModelLayerLocation(IdentifierUtil.default("emberreign"), "main")

        fun getTexturedModelData(): LayerDefinition {
            val modelData = MeshDefinition()
            val modelPartData = modelData.root

            val body = modelPartData.createEmptyChild(PartNames.BODY)
            val head = modelPartData.createEmptyChild(PartNames.HEAD)
            val hat = head.createEmptyChild(PartNames.HAT)
            val left_arm = modelPartData.createEmptyChild(PartNames.LEFT_ARM)
            val right_arm = modelPartData.createEmptyChild(PartNames.RIGHT_ARM)

            val left_leg = modelPartData.addOrReplaceChild(PartNames.LEFT_LEG, CubeListBuilder.create(), PartPose.offset(2.0f, 12.0f, 0.0f))

            val left_boot = left_leg.addOrReplaceChild(
                "left_boot", CubeListBuilder.create().texOffs(33, 21).addBox(-2.5f, 6.5f, -2.5f, 5.0f, 6.0f, 5.0f, CubeDeformation(-0.15f))
                    .texOffs(31, 33).addBox(-3.0f, 10.0f, -3.0f, 6.0f, 3.0f, 6.0f, CubeDeformation(-0.25f)), PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val left_feather_ribbon = left_boot.addOrReplaceChild(
                "left_feather_ribbon", CubeListBuilder.create().texOffs(29, 11).addBox(-3.5f, -1.0f, -3.5f, 7.0f, 2.0f, 7.0f, CubeDeformation(-0.5f))
                    .texOffs(37, 0).addBox(2.4f, -4.5f, -2.5f, 1.0f, 5.0f, 5.0f, CubeDeformation(-0.5f)), PartPose.offsetAndRotation(0.0f, 9.0f, 0.0f, -0.0873f, 0.0f, 0.0f)
            )

            val right_leg = modelPartData.addOrReplaceChild(PartNames.RIGHT_LEG, CubeListBuilder.create(), PartPose.offset(-2.0f, 12.0f, 0.0f))

            val right_boot = right_leg.addOrReplaceChild(
                "right_boot", CubeListBuilder.create().texOffs(2, 33).addBox(-3.0f, 10.0f, -3.0f, 6.0f, 3.0f, 6.0f, CubeDeformation(-0.25f))
                    .texOffs(4, 21).addBox(-2.5f, 6.5f, -2.5f, 5.0f, 6.0f, 5.0f, CubeDeformation(-0.15f)), PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val right_feather_ribbon = right_boot.addOrReplaceChild(
                "right_feather_ribbon", CubeListBuilder.create().texOffs(0, 11).addBox(-3.5f, -1.0f, -3.5f, 7.0f, 2.0f, 7.0f, CubeDeformation(-0.5f))
                    .texOffs(8, 0).addBox(-3.4f, -4.5f, -2.5f, 1.0f, 5.0f, 5.0f, CubeDeformation(-0.5f)), PartPose.offsetAndRotation(0.0f, 9.0f, 0.0f, -0.0873f, 0.0f, 0.0f)
            )
            return LayerDefinition.create(modelData, 64, 64)
        }
    }

    override fun setupAnim(renderState: S) {}
}