package de.fuballer.mcendgame.client.component.item.custom.armor.model.emberchant

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

class EmberchantModel<S : HumanoidRenderState>(
    root: ModelPart
) : HumanoidModel<S>(root) {
    companion object {
        val MODEL_LAYER = ModelLayerLocation(IdentifierUtil.default("emberchant"), "main")

        fun getTexturedModelData(): LayerDefinition {
            val modelData = MeshDefinition()
            val modelPartData = modelData.root

            val head = modelPartData.createEmptyChild(PartNames.HEAD)
            val hat = head.createEmptyChild(PartNames.HAT)
            val body = modelPartData.createEmptyChild(PartNames.BODY)
            val right_arm = modelPartData.createEmptyChild(PartNames.RIGHT_ARM)
            val left_arm = modelPartData.createEmptyChild(PartNames.LEFT_ARM)
            val right_leg = modelPartData.createEmptyChild(PartNames.RIGHT_LEG)
            val left_leg = modelPartData.createEmptyChild(PartNames.LEFT_LEG)

            val helmet = head.addOrReplaceChild("helmet", CubeListBuilder.create(), PartPose.offset(0.0f, 0.0f, 0.0f))

            val brim = helmet.addOrReplaceChild(
                "brim", CubeListBuilder.create().texOffs(0, 47).addBox(-8.0f, 0.0f, -8.0f, 16.0f, 1.0f, 16.0f, CubeDeformation(0.0f))
                    .texOffs(5, 41).addBox(-6.0f, 0.0f, -9.0f, 12.0f, 1.0f, 1.0f, CubeDeformation(0.0f)), PartPose.offsetAndRotation(0.0f, -5.5f, 0.0f, -0.0873f, 0.0435f, 0.0175f)
            )

            val brimLeft_r1 = brim.addOrReplaceChild(
                "brimLeft_r1",
                CubeListBuilder.create().texOffs(5, 44).addBox(-5.0f, 0.0f, -8.0f, 12.0f, 1.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(-1.0f, 0.0f, 1.0f, 0.0f, 1.5708f, 0.0f)
            )

            val brimRight_r1 = brim.addOrReplaceChild(
                "brimRight_r1",
                CubeListBuilder.create().texOffs(33, 41).addBox(-5.0f, 0.0f, -8.0f, 12.0f, 1.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(1.0f, 0.0f, -1.0f, 0.0f, -1.5708f, 0.0f)
            )

            val brimBack_r1 = brim.addOrReplaceChild(
                "brimBack_r1",
                CubeListBuilder.create().texOffs(33, 44).addBox(-5.0f, 0.0f, -8.0f, 12.0f, 1.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(1.0f, 0.0f, 1.0f, 0.0f, 3.1416f, 0.0f)
            )

            val base = brim.addOrReplaceChild(
                "base",
                CubeListBuilder.create().texOffs(0, 26).addBox(-10.0f, -3.75f, 0.0f, 10.0f, 4.0f, 10.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(5.0f, -0.25f, -4.75f, -0.0314f, -0.0436f, -0.0436f)
            )

            val band = base.addOrReplaceChild(
                "band",
                CubeListBuilder.create().texOffs(0, 10).addBox(-10.5f, -3.6f, -0.5f, 11.0f, 4.0f, 11.0f, CubeDeformation(-0.25f)),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0262f)
            )

            val tip0 = base.addOrReplaceChild(
                "tip0",
                CubeListBuilder.create().texOffs(40, 30).addBox(0.0f, -3.7642f, 0.0906f, 6.0f, 4.0f, 6.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(-8.0f, -3.95f, 1.75f, -0.2611f, 0.0151f, 0.0859f)
            )

            val tip1 = tip0.addOrReplaceChild(
                "tip1",
                CubeListBuilder.create().texOffs(46, 23).addBox(-3.0f, -3.0f, 0.0f, 3.0f, 3.0f, 3.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(4.5f, -3.75f, 1.25f, -0.5655f, -0.0468f, -0.0737f)
            )
            return LayerDefinition.create(modelData, 64, 64)
        }
    }

    override fun setupAnim(renderState: S) {}
}