package de.fuballer.mcendgame.client.component.item.custom.armor.model.stoneward

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

class StonewardModel<S : HumanoidRenderState>(
    root: ModelPart
) : HumanoidModel<S>(root) {
    companion object {
        val MODEL_LAYER = ModelLayerLocation(IdentifierUtil.default("stoneward"), "main")

        fun getTexturedModelData(): LayerDefinition {
            val modelData = MeshDefinition()
            val modelPartData = modelData.root

            val head = modelPartData.createEmptyChild(PartNames.HEAD)
            val hat = head.createEmptyChild(PartNames.HAT)
            val left_arm = modelPartData.createEmptyChild(PartNames.LEFT_ARM)
            val right_arm = modelPartData.createEmptyChild(PartNames.RIGHT_ARM)

            val body = modelPartData.createEmptyChild(PartNames.BODY)

            val leggings_waist =
                body.addOrReplaceChild(
                    "leggings_waist",
                    CubeListBuilder.create().texOffs(13, 0).addBox(-4.0F, 8.0F, -2.0F, 8.0F, 4.0F, 4.0F, CubeDeformation(0.55F)),
                    PartPose.offset(0.0F, 0.0F, 0.0F)
                )

            val left_leg = modelPartData.addOrReplaceChild(PartNames.LEFT_LEG, CubeListBuilder.create(), PartPose.offset(2.0f, 12.0f, 0.0f))

            val left_leggings = left_leg.addOrReplaceChild(
                "left_leggings", CubeListBuilder.create().texOffs(30, 47).addBox(-2.1F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, CubeDeformation(0.5F))
                    .texOffs(28, 28).addBox(-2.6F, -0.5F, -2.5F, 5.0F, 13.0F, 5.0F, CubeDeformation(0.23F))
                    .texOffs(26, 9).addBox(-2.9F, -5.5F, -3.0F, 6.0F, 12.0F, 6.0F, CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F)
            )

            val right_leg = modelPartData.addOrReplaceChild(PartNames.RIGHT_LEG, CubeListBuilder.create(), PartPose.offset(-2.0f, 12.0f, 0.0f))

            val right_leggings = right_leg.addOrReplaceChild(
                "right_leggings", CubeListBuilder.create().texOffs(4, 47).addBox(-1.9F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, CubeDeformation(0.5F))
                    .texOffs(2, 28).addBox(-2.4F, -0.5F, -2.5F, 5.0F, 13.0F, 5.0F, CubeDeformation(0.23F))
                    .texOffs(0, 9).addBox(-3.1F, -5.5F, -3.0F, 6.0F, 12.0F, 6.0F, CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F)
            )

            return LayerDefinition.create(modelData, 64, 64)
        }
    }

    override fun setupAnim(renderState: S) {}
}