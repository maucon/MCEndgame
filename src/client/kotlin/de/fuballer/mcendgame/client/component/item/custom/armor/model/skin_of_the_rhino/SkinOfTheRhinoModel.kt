package de.fuballer.mcendgame.client.component.item.custom.armor.model.skin_of_the_rhino

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

class SkinOfTheRhinoModel<S : HumanoidRenderState>(
    root: ModelPart
) : HumanoidModel<S>(root) {
    companion object {
        val MODEL_LAYER = ModelLayerLocation(IdentifierUtil.default("skin_of_the_rhino"), "main")

        fun getTexturedModelData(): LayerDefinition {
            val meshdefinition = MeshDefinition()
            val partdefinition = meshdefinition.root

            val head = partdefinition.createEmptyChild(PartNames.HEAD)
            val hat = head.createEmptyChild(PartNames.HAT)
            val right_leg = partdefinition.createEmptyChild(PartNames.RIGHT_LEG)
            val left_leg = partdefinition.createEmptyChild(PartNames.LEFT_LEG)

            val body = partdefinition.addOrReplaceChild(
                PartNames.BODY, CubeListBuilder.create().texOffs(27, 16).addBox(-5.0f, 3.0f, -3.0f, 10.0f, 9.0f, 6.0f, CubeDeformation(-0.1f))
                    .texOffs(25, 0).addBox(-5.5f, -0.5f, -3.5f, 11.0f, 8.0f, 7.0f, CubeDeformation(0.1f)), PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val front_horn = body.addOrReplaceChild(
                "front_horn",
                CubeListBuilder.create().texOffs(0, 21).addBox(-1.0f, -3.0f, -1.0f, 2.0f, 3.0f, 2.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, 5.75f, -2.75f, 0.6109f, 0.0f, 0.0f)
            )

            val upper_back_horn = body.addOrReplaceChild(
                "upper_back_horn",
                CubeListBuilder.create().texOffs(0, 27).addBox(-1.0f, -3.0f, -1.0f, 2.0f, 3.0f, 2.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, 4.75f, 2.75f, -0.6109f, 0.0f, 0.0f)
            )

            val left_arm = partdefinition.addOrReplaceChild(PartNames.LEFT_ARM, CubeListBuilder.create(), PartPose.offset(5.0f, 2.0f, 0.0f))

            val left_pauldron = left_arm.addOrReplaceChild(
                "left_pauldron",
                CubeListBuilder.create().texOffs(62, 0).addBox(0.0f, -1.0f, -3.0f, 6.0f, 10.0f, 6.0f, CubeDeformation(0.0f)),
                PartPose.offset(-2.0f, -1.75f, 0.0f)
            )

            val horn_r1 = left_pauldron.addOrReplaceChild(
                "horn_r1",
                CubeListBuilder.create().texOffs(54, 16).addBox(-1.0f, -2.0f, -1.0f, 2.0f, 3.0f, 2.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(2.75f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0908f)
            )

            val left_horn_0 = left_pauldron.addOrReplaceChild(
                "left_horn_0",
                CubeListBuilder.create().texOffs(61, 25).addBox(-3.0f, -3.0f, -1.5f, 3.0f, 3.0f, 3.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(6.0f, 3.5f, 0.0f, 0.0f, 0.0f, 1.1345f)
            )

            val left_horn_1 = left_horn_0.addOrReplaceChild(
                "left_horn_1",
                CubeListBuilder.create().texOffs(63, 19).addBox(-2.0f, -3.0f, -0.5f, 2.0f, 3.0f, 2.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(-0.5f, -3.0f, -0.5f, 0.0248f, 0.0223f, -0.3472f)
            )

            val right_arm = partdefinition.addOrReplaceChild(PartNames.RIGHT_ARM, CubeListBuilder.create(), PartPose.offset(-5.0f, 2.0f, 0.0f))

            val right_pauldron = right_arm.addOrReplaceChild(
                "right_pauldron",
                CubeListBuilder.create().texOffs(0, 0).addBox(-6.0f, -1.0f, -3.0f, 6.0f, 10.0f, 6.0f, CubeDeformation(0.0f)),
                PartPose.offset(2.0f, -1.75f, 0.0f)
            )

            val horn_r2 = right_pauldron.addOrReplaceChild(
                "horn_r2",
                CubeListBuilder.create().texOffs(24, 16).addBox(-1.0f, -2.0f, -1.0f, 2.0f, 3.0f, 2.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(-2.75f, -0.5f, 0.0f, 0.0f, 0.0f, -1.0908f)
            )

            val right_horn_0 = right_pauldron.addOrReplaceChild(
                "right_horn_0",
                CubeListBuilder.create().texOffs(13, 25).addBox(0.0f, -3.0f, -1.5f, 3.0f, 3.0f, 3.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(-6.0f, 3.5f, 0.0f, 0.0f, 0.0f, -1.1345f)
            )

            val right_horn_1 = right_horn_0.addOrReplaceChild(
                "right_horn_1",
                CubeListBuilder.create().texOffs(15, 19).addBox(0.0f, -3.0f, -0.5f, 2.0f, 3.0f, 2.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.5f, -3.0f, -0.5f, 0.0248f, -0.0223f, 0.3472f)
            )

            return LayerDefinition.create(meshdefinition, 128, 32)
        }
    }

    override fun setupAnim(renderState: S) {}
}