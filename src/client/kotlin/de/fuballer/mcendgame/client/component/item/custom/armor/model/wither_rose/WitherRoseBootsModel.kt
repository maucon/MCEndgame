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

class WitherRoseBootsModel<S : HumanoidRenderState>(
    root: ModelPart,
) : HumanoidModel<S>(root) {
    companion object {
        val MODEL_LAYER = ModelLayerLocation(IdentifierUtil.default("wither_rose_boots"), "main")

        fun getTexturedModelData(): LayerDefinition {
            val modelData = MeshDefinition()
            val modelPartData = modelData.root

            val head = modelPartData.createEmptyChild(PartNames.HEAD)
            val hat = head.createEmptyChild(PartNames.HAT)
            val body = modelPartData.createEmptyChild(PartNames.BODY)
            val left_arm = modelPartData.createEmptyChild(PartNames.LEFT_ARM)
            val right_arm = modelPartData.createEmptyChild(PartNames.RIGHT_ARM)

            val left_leg = modelPartData.addOrReplaceChild(PartNames.LEFT_LEG, CubeListBuilder.create(), PartPose.offset(2.0f, 12.0f, 0.0f))

            val left_boot = left_leg.addOrReplaceChild(
                "left_boot",
                CubeListBuilder.create().texOffs(22, 116).addBox(-2.5f, 5.5f, -2.6f, 5.0f, 7.0f, 5.0f, CubeDeformation(0.25f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val right_leg = modelPartData.addOrReplaceChild(PartNames.RIGHT_LEG, CubeListBuilder.create(), PartPose.offset(-2.0f, 12.0f, 0.0f))

            val right_boot = right_leg.addOrReplaceChild(
                "right_boot",
                CubeListBuilder.create().texOffs(0, 116).addBox(-2.5f, 5.5f, -2.5f, 5.0f, 7.0f, 5.0f, CubeDeformation(0.25f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )
            return LayerDefinition.create(modelData, 128, 128)
        }
    }

    override fun setupAnim(renderState: S) {}
}