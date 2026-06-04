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

class SuedeHelmetModel<S : HumanoidRenderState>(
    root: ModelPart,
) : HumanoidModel<S>(root) {
    companion object {
        val MODEL_LAYER = ModelLayerLocation(IdentifierUtil.default("suede_helmet"), "main")

        fun getTexturedModelData(): LayerDefinition {
            val modelData = MeshDefinition()
            val modelPartData = modelData.root

            val body = modelPartData.createEmptyChild(PartNames.BODY)
            val left_leg = modelPartData.createEmptyChild(PartNames.LEFT_LEG)
            val right_leg = modelPartData.createEmptyChild(PartNames.RIGHT_LEG)
            val left_arm = modelPartData.createEmptyChild(PartNames.LEFT_ARM)
            val right_arm = modelPartData.createEmptyChild(PartNames.RIGHT_ARM)

            val head = modelPartData.addOrReplaceChild(PartNames.HEAD, CubeListBuilder.create(), PartPose.offset(0.0f, 0.0f, 0.0f))
            val hat = head.createEmptyChild(PartNames.HAT)

            val helmet = head.addOrReplaceChild(
                "helmet", CubeListBuilder.create().texOffs(15, 67).addBox(-4.5f, -8.5f, -4.5f, 9.0f, 5.0f, 9.0f, CubeDeformation(0.0f))
                    .texOffs(15, 71).addBox(-1.75f, -7.0f, 4.5f, 3.0f, 3.0f, 1.0f, CubeDeformation(0.0f)), PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val scarf_band_long = helmet.addOrReplaceChild(
                "scarf_band_long",
                CubeListBuilder.create().texOffs(43, 69).addBox(-1.0f, 0.0f, 0.0f, 2.0f, 6.0f, 0.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(-0.4f, -4.1f, 5.1f, 0.0f, -0.1309f, 0.0436f)
            )

            val scarf_band_short = helmet.addOrReplaceChild(
                "scarf_band_short",
                CubeListBuilder.create().texOffs(48, 71).addBox(-0.5f, 0.0f, 0.0f, 1.0f, 4.0f, 0.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.3f, -4.1f, 4.9f, 0.0f, 0.1309f, -0.1309f)
            )
            return LayerDefinition.create(modelData, 128, 128)
        }
    }

    override fun setupAnim(renderState: S) {}
}