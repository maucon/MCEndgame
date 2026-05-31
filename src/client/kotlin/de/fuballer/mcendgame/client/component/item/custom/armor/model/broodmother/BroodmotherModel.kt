package de.fuballer.mcendgame.client.component.item.custom.armor.model.broodmother

import de.fuballer.mcendgame.client.accessor.BipedEntityRenderStateAccessor
import de.fuballer.mcendgame.client.component.item.custom.ModelPartDataExtension.createEmptyChild
import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.model.*
import net.minecraft.client.render.entity.model.BipedEntityModel
import net.minecraft.client.render.entity.model.EntityModelLayer
import net.minecraft.client.render.entity.model.EntityModelPartNames
import net.minecraft.client.render.entity.state.BipedEntityRenderState
import org.joml.Quaternionf
import kotlin.math.sin


private const val DEG_TO_RAD = Math.PI.toFloat() / 180f

class BroodmotherModel<S : BipedEntityRenderState>(
    root: ModelPart
) : BipedEntityModel<S>(root) {
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
        val MODEL_LAYER = EntityModelLayer(IdentifierUtil.default("broodmother"), "main")

        fun getTexturedModelData(): TexturedModelData {
            val modelData = ModelData()
            val modelPartData = modelData.root

            val head = modelPartData.createEmptyChild(EntityModelPartNames.HEAD)
            val hat = head.createEmptyChild(EntityModelPartNames.HAT)
            val left_leg = modelPartData.createEmptyChild(EntityModelPartNames.LEFT_LEG)
            val right_leg = modelPartData.createEmptyChild(EntityModelPartNames.RIGHT_LEG)

            val body = modelPartData.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create(), ModelTransform.origin(0.0f, 0.0f, 0.0f))

            val chestplate = body.addChild(
                "chestplate", ModelPartBuilder.create().uv(36, 13).cuboid(-4.0f, 4.0f, -2.0f, 8.0f, 8.0f, 4.0f, Dilation(0.15f))
                    .uv(34, 0).cuboid(-4.5f, 0.0f, -2.65f, 9.0f, 7.0f, 5.0f, Dilation(0.0f))
                    .uv(32, 3).cuboid(-3.5f, 3.0f, -2.75f, 7.0f, 2.0f, 0.0f, Dilation(0.0f)), ModelTransform.origin(0.0f, 0.0f, 0.0f)
            )

            val web = chestplate.addChild("web", ModelPartBuilder.create().uv(39, 37).cuboid(-4.5f, 0.5f, 0.0f, 9.0f, 15.0f, 0.0f, Dilation(0.0f)), ModelTransform.origin(0.0f, 2.0f, 2.95f))

            val legs =
                chestplate.addChild("legs", ModelPartBuilder.create().uv(42, 26).cuboid(-3.0f, -5.0f, 0.0f, 6.0f, 6.0f, 0.0f, Dilation(0.0f)), ModelTransform.origin(0.0f, 6.0f, 2.85f))

            val left_legs = legs.addChild("left_legs", ModelPartBuilder.create(), ModelTransform.origin(3.0f, 0.0f, 0.0f))

            val left_top_leg = left_legs.addChild(
                "left_top_leg",
                ModelPartBuilder.create().uv(59, 31).cuboid(0.0f, -5.0f, 0.0f, 11.0f, 10.0f, 0.0f, Dilation(0.0f)),
                ModelTransform.origin(-0.5f, -5.0f, 0.2f)
            )

            val left_middle_leg = left_legs.addChild(
                "left_middle_leg",
                ModelPartBuilder.create().uv(59, 42).cuboid(0.0f, -8.0f, 0.0f, 11.0f, 10.0f, 0.0f, Dilation(0.0f)),
                ModelTransform.origin(-0.25f, -2.0f, 0.1f)
            )

            val left_bottom_leg = left_legs.addChild(
                "left_bottom_leg",
                ModelPartBuilder.create().uv(59, 53).cuboid(-1.0f, -8.5f, 0.0f, 11.0f, 10.0f, 0.0f, Dilation(0.0f)),
                ModelTransform.origin(0.75f, -1.5f, 0.0f)
            )

            val right_legs = legs.addChild("right_legs", ModelPartBuilder.create(), ModelTransform.origin(-3.0f, 0.0f, 0.0f))

            val right_top_leg = right_legs.addChild(
                "right_top_leg",
                ModelPartBuilder.create().uv(16, 31).cuboid(-11.0f, -5.0f, 0.0f, 11.0f, 10.0f, 0.0f, Dilation(0.0f)),
                ModelTransform.origin(0.5f, -5.0f, 0.2f)
            )

            val right_middle_leg = right_legs.addChild(
                "right_middle_leg",
                ModelPartBuilder.create().uv(16, 42).cuboid(-11.0f, -8.0f, 0.0f, 11.0f, 10.0f, 0.0f, Dilation(0.0f)),
                ModelTransform.origin(0.25f, -2.0f, 0.1f)
            )

            val right_bottom_leg = right_legs.addChild(
                "right_bottom_leg",
                ModelPartBuilder.create().uv(16, 53).cuboid(-10.0f, -8.5f, 0.0f, 11.0f, 10.0f, 0.0f, Dilation(0.0f)),
                ModelTransform.origin(-0.75f, -1.5f, 0.0f)
            )

            val left_arm = modelPartData.addChild(EntityModelPartNames.LEFT_ARM, ModelPartBuilder.create(), ModelTransform.origin(5.0f, 2.0f, 0.0f))

            val chestplate_left_arm = left_arm.addChild(
                "chestplate_left_arm",
                ModelPartBuilder.create().uv(61, 11).cuboid(-1.0f, 2.0f, -2.0f, 4.0f, 7.0f, 4.0f, Dilation(0.1f)),
                ModelTransform.origin(0.0f, 0.0f, 0.0f)
            )

            val left_pauldron = chestplate_left_arm.addChild(
                "left_pauldron",
                ModelPartBuilder.create().uv(63, 1).cuboid(-1.25f, -2.5f, -2.25f, 5.0f, 4.0f, 5.0f, Dilation(0.0f)),
                ModelTransform.origin(0.0f, 0.0f, 0.0f)
            )

            val left_web = left_pauldron.addChild(
                "left_web",
                ModelPartBuilder.create().uv(78, 11).cuboid(-1.25f, -2.25f, -1.25f, 5.0f, 15.0f, 4.0f, Dilation(-0.1f)),
                ModelTransform.origin(0.0f, 0.0f, 0.0f)
            )

            val right_arm = modelPartData.addChild(EntityModelPartNames.RIGHT_ARM, ModelPartBuilder.create(), ModelTransform.origin(-5.0f, 2.0f, 0.0f))

            val chestplate_right_arm = right_arm.addChild(
                "chestplate_right_arm",
                ModelPartBuilder.create().uv(19, 11).cuboid(-3.0f, 2.0f, -2.0f, 4.0f, 7.0f, 4.0f, Dilation(0.1f)),
                ModelTransform.origin(0.0f, 0.0f, 0.0f)
            )

            val right_pauldron = chestplate_right_arm.addChild(
                "right_pauldron",
                ModelPartBuilder.create().uv(13, 1).cuboid(-3.75f, -2.5f, -2.25f, 5.0f, 4.0f, 5.0f, Dilation(0.0f)),
                ModelTransform.origin(0.0f, 0.0f, 0.0f)
            )

            val right_web = right_pauldron.addChild(
                "right_web",
                ModelPartBuilder.create().uv(0, 11).cuboid(-3.75f, -2.25f, -1.25f, 5.0f, 15.0f, 4.0f, Dilation(-0.1f)),
                ModelTransform.origin(0.0f, 0.0f, 0.0f)
            )
            return TexturedModelData.of(modelData, 128, 64)
        }
    }

    override fun setAngles(renderState: S) {
        resetNotCopiedTransforms()
        setCapeAngles(renderState)
    }

    private fun resetNotCopiedTransforms() {
        capeWeb.resetTransform()
        leftWeb.resetTransform()
        rightWeb.resetTransform()
        legs.resetTransform()
        leftLegs.resetTransform()
        rightLegs.resetTransform()
        leftTopLeg.resetTransform()
        leftMiddleLeg.resetTransform()
        leftBottomLeg.resetTransform()
        rightTopLeg.resetTransform()
        rightMiddleLeg.resetTransform()
        rightBottomLeg.resetTransform()
    }

    private fun setCapeAngles(renderState: S) {
        val accessor = renderState as BipedEntityRenderStateAccessor

        val verticalLift = accessor.`mcendgame$getCapeVerticalLift`()
        val forwardDrag = accessor.`mcendgame$getCapeForwardDrag`()
        val sidewaysSway = accessor.`mcendgame$getCapeSidewaysSway`()

        capeWeb.rotate(
            Quaternionf()
                .rotateX((6.0f + forwardDrag / 2.0f + verticalLift) * DEG_TO_RAD)
                .rotateZ(sidewaysSway / 2.0f * DEG_TO_RAD)
        )

        val maxWebAngle = 0.5f
        val amplitude = renderState.limbSwingAmplitude
        leftWeb.pitch += (1f - leftArm.pitch.coerceIn(-1f, 1f)) / 2f * maxWebAngle * amplitude
        rightWeb.pitch += (1f - rightArm.pitch.coerceIn(-1f, 1f)) / 2f * maxWebAngle * amplitude

        val time = renderState.age + renderState.partialTick

        val leftWave = sin(time * 0.10f + 0.4f) * 0.02f
        val rightWave = sin(time * 0.13f + 2.1f) * 0.02f

        leftTopLeg.roll += leftWave
        leftMiddleLeg.roll -= leftWave * 0.9f
        leftBottomLeg.roll += leftWave * 0.7f

        rightTopLeg.roll += rightWave
        rightMiddleLeg.roll -= rightWave * 0.9f
        rightBottomLeg.roll += rightWave * 0.7f

        legs.pitch -= 0.05f

        val yawWave = sin(time * 0.07f + 1.3f) * 0.02f
        leftLegs.yaw -= 0.04f + yawWave
        rightLegs.yaw += 0.04f + yawWave
    }
}