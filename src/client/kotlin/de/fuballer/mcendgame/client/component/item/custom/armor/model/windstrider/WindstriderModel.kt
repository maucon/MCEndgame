package de.fuballer.mcendgame.client.component.item.custom.armor.model.windstrider

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

class WindstriderModel<S : HumanoidRenderState>(
    root: ModelPart
) : HumanoidModel<S>(root) {
    companion object {
        val MODEL_LAYER = ModelLayerLocation(IdentifierUtil.default("windstrider"), "main")

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
                    CubeListBuilder.create().texOffs(9, 0).addBox(-4.0f, 7.0f, -2.0f, 8.0f, 5.0f, 4.0f, CubeDeformation(0.09f)),
                    PartPose.offset(0.0f, 0.0f, 0.0f)
                )

            val left_leg = modelPartData.addOrReplaceChild(PartNames.LEFT_LEG, CubeListBuilder.create(), PartPose.offset(2.0f, 12.0f, 0.0f))

            val left_leggings = left_leg.addOrReplaceChild(
                "left_leggings", CubeListBuilder.create().texOffs(24, 28).addBox(-2.0f, -0.9f, -2.045f, 4.0f, 11.0f, 4.0f, CubeDeformation(0.05f))
                    .texOffs(22, 10).addBox(-2.5f, -1.0f, -2.55f, 5.0f, 12.0f, 5.0f, CubeDeformation(0.05f)), PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val right_leg = modelPartData.addOrReplaceChild(PartNames.RIGHT_LEG, CubeListBuilder.create(), PartPose.offset(-2.0f, 12.0f, 0.0f))

            val right_leggings = right_leg.addOrReplaceChild(
                "right_leggings", CubeListBuilder.create().texOffs(2, 28).addBox(-2.0f, -0.9f, -2.05f, 4.0f, 11.0f, 4.0f, CubeDeformation(0.05f))
                    .texOffs(0, 10).addBox(-2.5f, -1.0f, -2.5f, 5.0f, 12.0f, 5.0f, CubeDeformation(0.05f)), PartPose.offset(0.0f, 0.0f, 0.0f)
            )
            return LayerDefinition.create(modelData, 64, 64)
        }
    }

    override fun setupAnim(renderState: S) {}
}