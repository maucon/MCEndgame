package de.fuballer.mcendgame.client.component.item.custom.armor.model.gilded_tempest

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

class GildedTempestModel<S : HumanoidRenderState>(
    root: ModelPart
) : HumanoidModel<S>(root) {
    companion object {
        val MODEL_LAYER = ModelLayerLocation(IdentifierUtil.default("gilded_tempest"), "main")

        fun getTexturedModelData(): LayerDefinition {
            val modelData = MeshDefinition()
            val modelPartData = modelData.root

            val head = modelPartData.createEmptyChild(PartNames.HEAD)
            val hat = head.createEmptyChild(PartNames.HAT)
            val left_arm = modelPartData.createEmptyChild(PartNames.LEFT_ARM)
            val right_arm = modelPartData.createEmptyChild(PartNames.RIGHT_ARM)

            val body = modelPartData.createEmptyChild(PartNames.BODY)

            val leggings_waist = body.addOrReplaceChild(
                "leggings_waist", CubeListBuilder.create().texOffs(18, 0).addBox(-4.5F, 6.5F, -2.5F, 9.0F, 5.0F, 5.0F, CubeDeformation(0.0F))
                    .texOffs(17, 39).addBox(-5.0F, 9.0F, -2.5F, 10.0F, 3.0F, 5.0F, CubeDeformation(0.1F))
                    .texOffs(15, 10).addBox(-5.5F, 7.5F, -3.0F, 11.0F, 5.0F, 6.0F, CubeDeformation(-0.05F)), PartPose.offset(0.0F, 0.0F, 0.0F)
            )

            val left_leg = modelPartData.addOrReplaceChild(PartNames.LEFT_LEG, CubeListBuilder.create(), PartPose.offset(2.0F, 12.0F, 0.0F))

            val left_leggings = left_leg.addOrReplaceChild(
                "left_leggings", CubeListBuilder.create().texOffs(43, 32).addBox(-2.1F, -0.5F, -2.5F, 5.0F, 6.0F, 5.0F, CubeDeformation(0.0F))
                    .texOffs(45, 52).addBox(-2.0F, 2.5F, -2.0F, 4.0F, 8.0F, 4.0F, CubeDeformation(0.05F))
                    .texOffs(44, 43).addBox(-1.5F, 5.5F, -2.5F, 4.0F, 4.0F, 5.0F, CubeDeformation(0.0F))
                    .texOffs(42, 22).addBox(-1.5F, 0.5F, -3.0F, 5.0F, 4.0F, 6.0F, CubeDeformation(-0.05F)), PartPose.offset(0.0F, 0.0F, 0.0F)
            )

            val right_leg = modelPartData.addOrReplaceChild(PartNames.RIGHT_LEG, CubeListBuilder.create(), PartPose.offset(-2.0F, 12.0F, 0.0F))

            val right_leggings = right_leg.addOrReplaceChild(
                "right_leggings", CubeListBuilder.create().texOffs(1, 32).addBox(-2.9F, -0.5F, -2.5F, 5.0F, 6.0F, 5.0F, CubeDeformation(0.0F))
                    .texOffs(3, 52).addBox(-2.0F, 2.5F, -2.0F, 4.0F, 8.0F, 4.0F, CubeDeformation(0.05F))
                    .texOffs(2, 43).addBox(-2.5F, 5.5F, -2.5F, 4.0F, 4.0F, 5.0F, CubeDeformation(0.0F))
                    .texOffs(0, 22).addBox(-3.5F, 0.5F, -3.0F, 5.0F, 4.0F, 6.0F, CubeDeformation(-0.05F)), PartPose.offset(0.0F, 0.0F, 0.0F)
            )
            return LayerDefinition.create(modelData, 64, 64)
        }
    }

    override fun setupAnim(renderState: S) {}
}