package de.fuballer.mcendgame.client.component.item.custom.armor.model.crown_of_the_stag

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

class CrownOfTheStagModel<S : HumanoidRenderState>(
    root: ModelPart
) : HumanoidModel<S>(root) {
    companion object {
        val MODEL_LAYER = ModelLayerLocation(IdentifierUtil.default("crown_of_the_stag"), "main")

        fun getTexturedModelData(): LayerDefinition {
            val meshdefinition = MeshDefinition()
            val partdefinition = meshdefinition.root

            val head = partdefinition.createEmptyChild(PartNames.HEAD)
            val hat = head.createEmptyChild(PartNames.HAT)
            val body = partdefinition.createEmptyChild(PartNames.BODY)
            val right_arm = partdefinition.createEmptyChild(PartNames.RIGHT_ARM)
            val left_arm = partdefinition.createEmptyChild(PartNames.LEFT_ARM)
            val right_leg = partdefinition.createEmptyChild(PartNames.RIGHT_LEG)
            val left_leg = partdefinition.createEmptyChild(PartNames.LEFT_LEG)

            val wreath = head.addOrReplaceChild(
                "wreath",
                CubeListBuilder.create().texOffs(0, 0).addBox(-5.0f, -1.35f, -5.0f, 10.0f, 2.0f, 10.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, -5.4f, 0.0f, -0.0873f, 0.0f, 0.0f)
            )

            val antlers = head.addOrReplaceChild("antlers", CubeListBuilder.create(), PartPose.offset(0.0f, 0.25f, 0.0f))

            val leftAntlerBase = antlers.addOrReplaceChild("leftAntlerBase", CubeListBuilder.create(), PartPose.offsetAndRotation(2.75f, -8.5f, 0.5f, 0.1642f, -0.0594f, 1.2168f))

            val leftAntlerBaseTransform = leftAntlerBase.addOrReplaceChild(
                "leftAntlerBaseTransform",
                CubeListBuilder.create().texOffs(33, 25).addBox(0.0f, -4.0f, -2.0f, 2.0f, 4.0f, 2.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val leftAnterMainMiddle = leftAntlerBaseTransform.addOrReplaceChild(
                "leftAnterMainMiddle",
                CubeListBuilder.create().texOffs(33, 18).addBox(-0.25f, -3.75f, -2.0f, 2.0f, 4.0f, 2.0f, CubeDeformation(-0.25f)),
                PartPose.offsetAndRotation(0.25f, -4.0f, 0.0f, 0.0873f, 0.0f, 0.1745f)
            )

            val leftAntlerMainTip = leftAnterMainMiddle.addOrReplaceChild(
                "leftAntlerMainTip",
                CubeListBuilder.create().texOffs(35, 13).addBox(-1.01f, -3.02f, -1.04f, 1.0f, 3.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(1.348f, -3.3845f, -0.4126f, 0.1309f, 0.0f, -0.3491f)
            )

            val leftAntlerMainMiddleVines = leftAnterMainMiddle.addOrReplaceChild("leftAntlerMainMiddleVines", CubeListBuilder.create(), PartPose.offset(-0.1f, -2.0f, -1.0f))

            val vines_r1 = leftAntlerMainMiddleVines.addOrReplaceChild(
                "vines_r1",
                CubeListBuilder.create().texOffs(21, 32).addBox(-2.35f, -0.65f, -1.5f, 4.0f, 6.0f, 3.0f, CubeDeformation(-0.7f)),
                PartPose.offsetAndRotation(0.0f, -0.1f, 0.0f, 0.0f, 0.0f, -1.5708f)
            )

            val leftAntlerBranchBase = leftAntlerBaseTransform.addOrReplaceChild(
                "leftAntlerBranchBase",
                CubeListBuilder.create().texOffs(24, 18).addBox(-1.75f, -3.5f, -1.75f, 2.0f, 4.0f, 2.0f, CubeDeformation(-0.25f)),
                PartPose.offsetAndRotation(1.0f, -4.0f, -0.15f, 0.0003f, -0.0076f, -1.0469f)
            )

            val leftAntlerBranchOuterTip = leftAntlerBranchBase.addOrReplaceChild(
                "leftAntlerBranchOuterTip",
                CubeListBuilder.create().texOffs(26, 13).addBox(-1.0f, -3.0f, -1.0f, 1.0f, 3.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, -2.5f, -0.25f, 0.0f, 0.0f, 0.3054f)
            )

            val leftAntlerBranchInnerTip = leftAntlerBranchBase.addOrReplaceChild(
                "leftAntlerBranchInnerTip",
                CubeListBuilder.create().texOffs(21, 14).addBox(0.0f, -2.0f, -1.0f, 1.0f, 2.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(-1.0f, -2.5f, -0.35f, 0.1309f, 0.0f, -0.8727f)
            )

            val leftAnterBranchVines = leftAntlerBranchBase.addOrReplaceChild("leftAnterBranchVines", CubeListBuilder.create(), PartPose.offset(-0.75f, -3.75f, -0.75f))

            val vines_r2 = leftAnterBranchVines.addOrReplaceChild(
                "vines_r2",
                CubeListBuilder.create().texOffs(21, 43).addBox(-1.5f, -0.75f, -1.5f, 3.0f, 4.0f, 3.0f, CubeDeformation(-0.7f)),
                PartPose.offsetAndRotation(0.0f, 0.1f, 0.0f, 0.0f, 0.0f, -0.1309f)
            )

            val leftAntlerSmallBranch = leftAntlerBaseTransform.addOrReplaceChild(
                "leftAntlerSmallBranch",
                CubeListBuilder.create().texOffs(28, 28).addBox(-0.0667f, -1.9259f, 0.0083f, 1.0f, 2.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.2702f, -0.9131f, -1.5604f, -0.211f, -0.056f, -1.9576f)
            )

            val rightAntlerBase = antlers.addOrReplaceChild("rightAntlerBase", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.75f, -8.5f, 0.5f, 0.1642f, 0.0594f, -1.2168f))

            val rightAntlerBaseTransform = rightAntlerBase.addOrReplaceChild(
                "rightAntlerBaseTransform",
                CubeListBuilder.create().texOffs(0, 25).addBox(-2.0f, -4.0f, -2.0f, 2.0f, 4.0f, 2.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val rightAnterMainMiddle = rightAntlerBaseTransform.addOrReplaceChild(
                "rightAnterMainMiddle",
                CubeListBuilder.create().texOffs(0, 18).addBox(-1.75f, -3.75f, -2.0f, 2.0f, 4.0f, 2.0f, CubeDeformation(-0.25f)),
                PartPose.offsetAndRotation(-0.25f, -4.0f, 0.0f, 0.0873f, 0.0f, -0.1745f)
            )

            val rightAntlerMainTip = rightAnterMainMiddle.addOrReplaceChild(
                "rightAntlerMainTip",
                CubeListBuilder.create().texOffs(2, 13).addBox(0.01f, -3.02f, -1.04f, 1.0f, 3.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(-1.348f, -3.3845f, -0.4126f, 0.1309f, 0.0f, 0.3491f)
            )

            val rightAntlerMainTipVines = rightAntlerMainTip.addOrReplaceChild("rightAntlerMainTipVines", CubeListBuilder.create(), PartPose.offset(1.2f, -2.15f, -0.5f))

            val vines_r3 = rightAntlerMainTipVines.addOrReplaceChild(
                "vines_r3",
                CubeListBuilder.create().texOffs(6, 42).addBox(-2.0f, -4.0f, -2.0f, 4.0f, 5.0f, 3.0f, CubeDeformation(-0.85f)),
                PartPose.offsetAndRotation(-3.2f, 0.55f, 0.5f, 0.0f, 0.0f, 1.5708f)
            )

            val rightAntlerMainMiddleVines = rightAnterMainMiddle.addOrReplaceChild("rightAntlerMainMiddleVines", CubeListBuilder.create(), PartPose.offset(0.1f, -1.35f, -1.0f))

            val vines_r4 = rightAntlerMainMiddleVines.addOrReplaceChild(
                "vines_r4",
                CubeListBuilder.create().texOffs(6, 32).addBox(-1.65f, -0.65f, -1.5f, 4.0f, 6.0f, 3.0f, CubeDeformation(-0.7f)),
                PartPose.offsetAndRotation(0.0f, -0.75f, 0.0f, 0.0f, 0.0f, 1.5708f)
            )

            val rightAntlerBranchBase = rightAntlerBaseTransform.addOrReplaceChild(
                "rightAntlerBranchBase",
                CubeListBuilder.create().texOffs(9, 18).addBox(-0.25f, -3.5f, -1.75f, 2.0f, 4.0f, 2.0f, CubeDeformation(-0.25f)),
                PartPose.offsetAndRotation(-1.0f, -4.0f, -0.15f, 0.0003f, 0.0076f, 1.0469f)
            )

            val rightAntlerBranchOuterTip = rightAntlerBranchBase.addOrReplaceChild(
                "rightAntlerBranchOuterTip",
                CubeListBuilder.create().texOffs(11, 13).addBox(0.0f, -3.0f, -1.0f, 1.0f, 3.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, -2.5f, -0.25f, 0.0f, 0.0f, -0.3054f)
            )

            val rightAntlerBranchInnerTip = rightAntlerBranchBase.addOrReplaceChild(
                "rightAntlerBranchInnerTip",
                CubeListBuilder.create().texOffs(16, 14).addBox(-1.0f, -2.0f, -1.0f, 1.0f, 2.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(1.0f, -2.5f, -0.35f, 0.1309f, 0.0f, 0.8727f)
            )

            val rightAntlerSmallBranch = rightAntlerBaseTransform.addOrReplaceChild(
                "rightAntlerSmallBranch",
                CubeListBuilder.create().texOffs(9, 28).addBox(-0.9333f, -1.9259f, 0.0083f, 1.0f, 2.0f, 1.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(-0.2702f, -0.9131f, -1.5604f, -0.211f, 0.056f, 1.9576f)
            )

            return LayerDefinition.create(meshdefinition, 64, 64)
        }
    }

    override fun setupAnim(renderState: S) {}
}