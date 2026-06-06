package de.fuballer.mcendgame.client.component.item.custom.armor.model.broodmother

import de.fuballer.mcendgame.client.accessor.BipedEntityRenderStateAccessor
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
import org.joml.Quaternionf
import kotlin.math.sin

private const val DEG_TO_RAD = Math.PI.toFloat() / 180f

class BroodmotherModel<S : HumanoidRenderState>(
    root: ModelPart
) : HumanoidModel<S>(root) {
    private val capeWeb: ModelPart
    private val leftWeb: ModelPart
    private val rightWeb: ModelPart
    private val legs: ModelPart
    private val leftLegs: ModelPart
    private val rightLegs: ModelPart
    private val leftTopLeg: ModelPart
    private val leftMiddleLeg: ModelPart
    private val leftBottomLeg: ModelPart
    private val rightTopLeg: ModelPart
    private val rightMiddleLeg: ModelPart
    private val rightBottomLeg: ModelPart

    init {
        val chestplate = body.getChild("chestplate")
        capeWeb = chestplate.getChild("web")

        val chestplateLeftArm = leftArm.getChild("chestplate_left_arm")
        val leftPauldron = chestplateLeftArm.getChild("left_pauldron")
        leftWeb = leftPauldron.getChild("left_web")

        val chestplateRightArm = rightArm.getChild("chestplate_right_arm")
        val rightPauldron = chestplateRightArm.getChild("right_pauldron")
        rightWeb = rightPauldron.getChild("right_web")

        legs = chestplate.getChild("legs")

        leftLegs = legs.getChild("left_legs")
        leftTopLeg = leftLegs.getChild("left_top_leg")
        leftMiddleLeg = leftLegs.getChild("left_middle_leg")
        leftBottomLeg = leftLegs.getChild("left_bottom_leg")

        rightLegs = legs.getChild("right_legs")
        rightTopLeg = rightLegs.getChild("right_top_leg")
        rightMiddleLeg = rightLegs.getChild("right_middle_leg")
        rightBottomLeg = rightLegs.getChild("right_bottom_leg")
    }

    companion object {
        val MODEL_LAYER = ModelLayerLocation(IdentifierUtil.default("broodmother"), "main")

        fun getTexturedModelData(): LayerDefinition {
            val modelData = MeshDefinition()
            val modelPartData = modelData.root

            val head = modelPartData.createEmptyChild(PartNames.HEAD)
            val hat = head.createEmptyChild(PartNames.HAT)
            val left_leg = modelPartData.createEmptyChild(PartNames.LEFT_LEG)
            val right_leg = modelPartData.createEmptyChild(PartNames.RIGHT_LEG)

            val body = modelPartData.addOrReplaceChild(PartNames.BODY, CubeListBuilder.create(), PartPose.offset(0.0f, 0.0f, 0.0f))

            val chestplate = body.addOrReplaceChild(
                "chestplate", CubeListBuilder.create().texOffs(36, 13).addBox(-4.0f, 4.0f, -2.0f, 8.0f, 8.0f, 4.0f, CubeDeformation(0.15f))
                    .texOffs(34, 0).addBox(-4.5f, 0.0f, -2.65f, 9.0f, 7.0f, 5.0f, CubeDeformation(0.0f))
                    .texOffs(32, 3).addBox(-3.5f, 3.0f, -2.75f, 7.0f, 2.0f, 0.0f, CubeDeformation(0.0f)), PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val web = chestplate.addOrReplaceChild(
                "web",
                CubeListBuilder.create().texOffs(39, 37).addBox(-4.5f, 0.5f, 0.0f, 9.0f, 15.0f, 0.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 2.0f, 2.95f)
            )

            val legs =
                chestplate.addOrReplaceChild(
                    "legs",
                    CubeListBuilder.create().texOffs(42, 26).addBox(-3.0f, -5.0f, 0.0f, 6.0f, 6.0f, 0.0f, CubeDeformation(0.0f)),
                    PartPose.offset(0.0f, 6.0f, 2.85f)
                )

            val left_legs = legs.addOrReplaceChild("left_legs", CubeListBuilder.create(), PartPose.offset(3.0f, 0.0f, 0.0f))

            val left_top_leg = left_legs.addOrReplaceChild(
                "left_top_leg",
                CubeListBuilder.create().texOffs(59, 31).addBox(0.0f, -5.0f, 0.0f, 11.0f, 10.0f, 0.0f, CubeDeformation(0.0f)),
                PartPose.offset(-0.5f, -5.0f, 0.2f)
            )

            val left_middle_leg = left_legs.addOrReplaceChild(
                "left_middle_leg",
                CubeListBuilder.create().texOffs(59, 42).addBox(0.0f, -8.0f, 0.0f, 11.0f, 10.0f, 0.0f, CubeDeformation(0.0f)),
                PartPose.offset(-0.25f, -2.0f, 0.1f)
            )

            val left_bottom_leg = left_legs.addOrReplaceChild(
                "left_bottom_leg",
                CubeListBuilder.create().texOffs(59, 53).addBox(-1.0f, -8.5f, 0.0f, 11.0f, 10.0f, 0.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.75f, -1.5f, 0.0f)
            )

            val right_legs = legs.addOrReplaceChild("right_legs", CubeListBuilder.create(), PartPose.offset(-3.0f, 0.0f, 0.0f))

            val right_top_leg = right_legs.addOrReplaceChild(
                "right_top_leg",
                CubeListBuilder.create().texOffs(16, 31).addBox(-11.0f, -5.0f, 0.0f, 11.0f, 10.0f, 0.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.5f, -5.0f, 0.2f)
            )

            val right_middle_leg = right_legs.addOrReplaceChild(
                "right_middle_leg",
                CubeListBuilder.create().texOffs(16, 42).addBox(-11.0f, -8.0f, 0.0f, 11.0f, 10.0f, 0.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.25f, -2.0f, 0.1f)
            )

            val right_bottom_leg = right_legs.addOrReplaceChild(
                "right_bottom_leg",
                CubeListBuilder.create().texOffs(16, 53).addBox(-10.0f, -8.5f, 0.0f, 11.0f, 10.0f, 0.0f, CubeDeformation(0.0f)),
                PartPose.offset(-0.75f, -1.5f, 0.0f)
            )

            val left_arm = modelPartData.addOrReplaceChild(PartNames.LEFT_ARM, CubeListBuilder.create(), PartPose.offset(5.0f, 2.0f, 0.0f))

            val chestplate_left_arm = left_arm.addOrReplaceChild(
                "chestplate_left_arm",
                CubeListBuilder.create().texOffs(61, 11).addBox(-1.0f, 2.0f, -2.0f, 4.0f, 7.0f, 4.0f, CubeDeformation(0.1f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val left_pauldron = chestplate_left_arm.addOrReplaceChild(
                "left_pauldron",
                CubeListBuilder.create().texOffs(63, 1).addBox(-1.25f, -2.5f, -2.25f, 5.0f, 4.0f, 5.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val left_web = left_pauldron.addOrReplaceChild(
                "left_web",
                CubeListBuilder.create().texOffs(78, 11).addBox(-1.25f, -2.25f, -1.25f, 5.0f, 15.0f, 4.0f, CubeDeformation(-0.1f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val right_arm = modelPartData.addOrReplaceChild(PartNames.RIGHT_ARM, CubeListBuilder.create(), PartPose.offset(-5.0f, 2.0f, 0.0f))

            val chestplate_right_arm = right_arm.addOrReplaceChild(
                "chestplate_right_arm",
                CubeListBuilder.create().texOffs(19, 11).addBox(-3.0f, 2.0f, -2.0f, 4.0f, 7.0f, 4.0f, CubeDeformation(0.1f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val right_pauldron = chestplate_right_arm.addOrReplaceChild(
                "right_pauldron",
                CubeListBuilder.create().texOffs(13, 1).addBox(-3.75f, -2.5f, -2.25f, 5.0f, 4.0f, 5.0f, CubeDeformation(0.0f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )

            val right_web = right_pauldron.addOrReplaceChild(
                "right_web",
                CubeListBuilder.create().texOffs(0, 11).addBox(-3.75f, -2.25f, -1.25f, 5.0f, 15.0f, 4.0f, CubeDeformation(-0.1f)),
                PartPose.offset(0.0f, 0.0f, 0.0f)
            )
            return LayerDefinition.create(modelData, 128, 64)
        }
    }

    override fun setupAnim(renderState: S) {
        resetNotCopiedTransforms()
        setCapeAngles(renderState)
    }

    private fun resetNotCopiedTransforms() {
        capeWeb.resetPose()
        leftWeb.resetPose()
        rightWeb.resetPose()
        legs.resetPose()
        leftLegs.resetPose()
        rightLegs.resetPose()
        leftTopLeg.resetPose()
        leftMiddleLeg.resetPose()
        leftBottomLeg.resetPose()
        rightTopLeg.resetPose()
        rightMiddleLeg.resetPose()
        rightBottomLeg.resetPose()
    }

    private fun setCapeAngles(renderState: S) {
        val accessor = renderState as BipedEntityRenderStateAccessor

        val verticalLift = accessor.`mcendgame$getCapeVerticalLift`()
        val forwardDrag = accessor.`mcendgame$getCapeForwardDrag`()
        val sidewaysSway = accessor.`mcendgame$getCapeSidewaysSway`()

        capeWeb.rotateBy(
            Quaternionf()
                .rotateX((6.0f + forwardDrag / 2.0f + verticalLift) * DEG_TO_RAD)
                .rotateZ(sidewaysSway / 2.0f * DEG_TO_RAD)
        )

        val maxWebAngle = 0.5f
        val amplitude = renderState.walkAnimationSpeed
        leftWeb.xRot += (1f - leftArm.xRot.coerceIn(-1f, 1f)) / 2f * maxWebAngle * amplitude
        rightWeb.xRot += (1f - rightArm.xRot.coerceIn(-1f, 1f)) / 2f * maxWebAngle * amplitude

        val time = renderState.ageInTicks + renderState.partialTick

        val leftWave = sin(time * 0.10f + 0.4f) * 0.02f
        val rightWave = sin(time * 0.13f + 2.1f) * 0.02f

        leftTopLeg.zRot += leftWave
        leftMiddleLeg.zRot -= leftWave * 0.9f
        leftBottomLeg.zRot += leftWave * 0.7f

        rightTopLeg.zRot += rightWave
        rightMiddleLeg.zRot -= rightWave * 0.9f
        rightBottomLeg.zRot += rightWave * 0.7f

        legs.xRot -= 0.05f

        val yawWave = sin(time * 0.07f + 1.3f) * 0.02f
        leftLegs.yRot -= 0.04f + yawWave
        rightLegs.yRot += 0.04f + yawWave
    }
}