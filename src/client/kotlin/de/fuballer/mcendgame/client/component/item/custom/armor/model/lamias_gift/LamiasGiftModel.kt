package de.fuballer.mcendgame.client.component.item.custom.armor.model.lamias_gift

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
import net.minecraft.client.renderer.entity.state.AvatarRenderState
import net.minecraft.client.renderer.entity.state.HumanoidRenderState
import kotlin.math.sin

class LamiasGiftModel<S : HumanoidRenderState>(
    root: ModelPart
) : HumanoidModel<S>(root) {
    private val leggings = body.getChild("leggings")
    private val tailZero: ModelPart = leggings.getChild("nagaTailZero")
    private val tailOne: ModelPart = tailZero.getChild("nagaTailOne")
    private val tailTwo: ModelPart = tailOne.getChild("nagaTailTwo")
    private val tailThree: ModelPart = tailTwo.getChild("nagaTailThree")
    private val tailFour: ModelPart = tailThree.getChild("nagaTailFour")
    private val tailFive: ModelPart = tailFour.getChild("nagaTailFive")

    companion object {
        val MODEL_LAYER = ModelLayerLocation(IdentifierUtil.default("lamias_gift"), "main")

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

            val leggings = body.addOrReplaceChild("leggings", CubeListBuilder.create(), PartPose.offset(0.0f, 24.0f, 0.0f))

            val nagaTailZero =
                leggings.addOrReplaceChild(
                    "nagaTailZero",
                    CubeListBuilder.create().texOffs(2, 1).addBox(-4.5f, 0.0f, -3.0f, 9.0f, 10.0f, 5.0f, CubeDeformation(0.0f)),
                    PartPose.offset(0.0f, -15.75f, 0.5f)
                )

            val nagaTailOne = nagaTailZero.addOrReplaceChild(
                "nagaTailOne",
                CubeListBuilder.create().texOffs(3, 16).addBox(-4.0f, -1.2145f, -2.6213f, 8.0f, 8.0f, 5.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, 9.0f, -0.25f, 0.7854f, 0.0f, 0.0f)
            )

            val nagaTailTwo = nagaTailOne.addOrReplaceChild(
                "nagaTailTwo",
                CubeListBuilder.create().texOffs(6, 30).addBox(-3.0f, -0.7951f, -2.2981f, 6.0f, 7.0f, 4.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, 5.7855f, -0.3713f, 0.7854f, 0.0f, 0.0f)
            )

            val nagaTailThree = nagaTailTwo.addOrReplaceChild(
                "nagaTailThree",
                CubeListBuilder.create().texOffs(9, 42).addBox(-2.0f, -0.5f, -1.5006f, 4.0f, 5.0f, 3.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 6.205f, -0.7981f)
            )

            val nagaTailFour = nagaTailThree.addOrReplaceChild(
                "nagaTailFour",
                CubeListBuilder.create().texOffs(11, 51).addBox(-1.5f, -0.5f, -1.0056f, 3.0f, 5.0f, 2.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 4.5f, -0.5f)
            )

            val nagaTailFive = nagaTailFour.addOrReplaceChild(
                "nagaTailFive",
                CubeListBuilder.create().texOffs(12, 58).addBox(-1.0f, -0.5f, -0.6806f, 2.0f, 4.0f, 2.0f, CubeDeformation(-0.25f)),
                PartPose.offset(0.0f, 4.5f, -0.5f)
            )
            return LayerDefinition.create(modelData, 32, 64)
        }
    }

    override fun setupAnim(renderState: S) {
        resetNotCopiedTransforms()
        setTailAngles(renderState)
    }

    private fun resetNotCopiedTransforms() {
        tailZero.resetPose()
        tailOne.resetPose()
        tailTwo.resetPose()
        tailThree.resetPose()
        tailFour.resetPose()
        tailFive.resetPose()
    }

    private fun setTailAngles(renderState: S) {
        val animSpeed = 7F
        val moveSpeed = renderState.walkAnimationSpeed // 0.0 - 1.0

        var tailOneYawFactor = 0.5F

        (renderState as? AvatarRenderState)?.let { playerState ->
            if (playerState.isVisuallySwimming || playerState.isFallFlying) {
                tailOne.xRot -= 0.8F
                tailTwo.xRot -= 0.8F
                tailOneYawFactor = 0.15F
            } else if (playerState.isCrouching) {
                tailTwo.xRot -= 0.45F
            }

            if (playerState.isPassenger) {
                tailZero.yScale *= 0.5F
                tailOne.zScale *= 2F
                tailOne.xRot += 0.8F

                tailTwo.xRot += 0.4F
                tailThree.xRot += 0.1F
            }
        }

        val tailZeroRoll = sin(renderState.ageInTicks / animSpeed) * moveSpeed * 0.1F
        tailZero.zRot += tailZeroRoll

        tailOne.zRot -= tailZeroRoll
        tailOne.yRot -= sin((renderState.ageInTicks - 7) / animSpeed) * moveSpeed * tailOneYawFactor

        tailTwo.zRot += sin((renderState.ageInTicks - 14) / animSpeed) * moveSpeed * 0.5F
        tailThree.zRot += sin((renderState.ageInTicks - 21) / animSpeed) * moveSpeed * 0.5F
        tailFour.zRot += sin((renderState.ageInTicks - 28) / animSpeed) * moveSpeed * 0.5F
        tailFive.zRot += sin((renderState.ageInTicks - 35) / animSpeed) * moveSpeed * 0.5F
    }
}