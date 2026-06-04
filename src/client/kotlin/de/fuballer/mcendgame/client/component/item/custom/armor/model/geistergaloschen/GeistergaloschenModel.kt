package de.fuballer.mcendgame.client.component.item.custom.armor.model.geistergaloschen

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

class GeistergaloschenModel<S : HumanoidRenderState>(
    root: ModelPart
) : HumanoidModel<S>(root) {
    companion object {
        val MODEL_LAYER = ModelLayerLocation(IdentifierUtil.default("geistergaloschen"), "main")

        fun getTexturedModelData(): LayerDefinition {
            val modelData = MeshDefinition()
            val modelPartData = modelData.root

            val head = modelPartData.createEmptyChild(PartNames.HEAD)
            val hat = head.createEmptyChild(PartNames.HAT)
            val left_arm = modelPartData.createEmptyChild(PartNames.LEFT_ARM)
            val right_arm = modelPartData.createEmptyChild(PartNames.RIGHT_ARM)
            val body = modelPartData.createEmptyChild(PartNames.BODY)

            val left_leg = modelPartData.addOrReplaceChild(PartNames.LEFT_LEG, CubeListBuilder.create(), PartPose.offset(2.0f, 12.0f, 0.0f))

            val left_boot = left_leg.addOrReplaceChild(
                "left_boot", CubeListBuilder.create().texOffs(21, 8).mirror().addBox(-2.6F, 6.5F, -2.5F, 5.0F, 6.0F, 5.0F, CubeDeformation(0.1F)).mirror(false)
                    .texOffs(21, 0).mirror().addBox(-2.6F, 6.5F, -2.5F, 5.0F, 2.0F, 5.0F, CubeDeformation(0.3F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F)
            )

            val right_leg = modelPartData.addOrReplaceChild(PartNames.RIGHT_LEG, CubeListBuilder.create(), PartPose.offset(-2.0f, 12.0f, 0.0f))

            val right_boot = right_leg.addOrReplaceChild(
                "right_boot", CubeListBuilder.create().texOffs(0, 8).addBox(-2.4F, 6.5F, -2.5F, 5.0F, 6.0F, 5.0F, CubeDeformation(0.1F))
                    .texOffs(0, 0).addBox(-2.4F, 6.5F, -2.5F, 5.0F, 2.0F, 5.0F, CubeDeformation(0.3F)), PartPose.offset(0.0F, 0.0F, 0.0F)
            )

            return LayerDefinition.create(modelData, 64, 32)
        }
    }

    override fun setupAnim(renderState: S) {}
}