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

class WitherRoseHelmetModel<S : HumanoidRenderState>(
    root: ModelPart,
) : HumanoidModel<S>(root) {
    companion object {
        val MODEL_LAYER = ModelLayerLocation(IdentifierUtil.default("wither_rose_helmet"), "main")

        fun getTexturedModelData(): LayerDefinition {
            val modelData = MeshDefinition()
            val modelPartData = modelData.root

            val body = modelPartData.createEmptyChild(PartNames.BODY)
            val left_arm = modelPartData.createEmptyChild(PartNames.LEFT_ARM)
            val right_arm = modelPartData.createEmptyChild(PartNames.RIGHT_ARM)
            val left_leg = modelPartData.createEmptyChild(PartNames.LEFT_LEG)
            val right_leg = modelPartData.createEmptyChild(PartNames.RIGHT_LEG)

            val head = modelPartData.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0f, 0.0f, 0.0f))
            val hat = head.createEmptyChild(PartNames.HAT)

            val helmet =
                head.addOrReplaceChild(
                    "helmet",
                    CubeListBuilder.create().texOffs(29, 46).addBox(-4.5f, -8.5f, -4.5f, 9.0f, 10.0f, 9.0f, CubeDeformation(0.25f)),
                    PartPose.offset(0.0f, 0.0f, 0.0f)
                )
            return LayerDefinition.create(modelData, 128, 128)
        }
    }

    override fun setupAnim(renderState: S) {}
}