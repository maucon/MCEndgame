package de.fuballer.mcendgame.client.component.entity.custom.entities.swamp_golem

import de.fuballer.mcendgame.main.util.minecraft.IdentifierUtil
import net.minecraft.client.animation.KeyframeAnimation
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.geom.ModelLayerLocation
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition

class SwampGolemEntityModel(
    modelPart: ModelPart,
) : EntityModel<SwampGolemRenderState>(modelPart) {
    val body = root.getChild("body")
    val lowerBody = body.getChild("lower_body")
    val upperBody = lowerBody.getChild("upper_body")
    val leftArm = upperBody.getChild("left_arm")
    val upperLeftArm = leftArm.getChild("upper_left_arm")
    val lowerLeftArm = upperLeftArm.getChild("lower_left_arm")
    val rightArm = upperBody.getChild("right_arm")
    val upperRightArm = rightArm.getChild("upper_right_arm")
    val lowerRightArm = upperRightArm.getChild("lower_right_arm")
    val head = upperBody.getChild("head")
    val leftLeg = lowerBody.getChild("left_leg")
    val upperLeftLeg = leftLeg.getChild("upper_left_leg")
    val lowerLeftLeg = upperLeftLeg.getChild("lower_left_leg")
    val rightLeg = lowerBody.getChild("right_leg")
    val upperRightLeg = rightLeg.getChild("upper_right_leg")
    val lowerRightLeg = upperRightLeg.getChild("lower_right_leg")

    val walkingAnimation: KeyframeAnimation = SwampGolemAnimations.WALKING.bake(modelPart)
    val idleAnimation: KeyframeAnimation = SwampGolemAnimations.IDLE.bake(modelPart)
    val slamAnimation: KeyframeAnimation = SwampGolemAnimations.SLAM.bake(modelPart)

    companion object {
        val SWAMP_GOLEM = ModelLayerLocation(IdentifierUtil.default("swamp_golem"), "main")

        fun getTexturedModelData(): LayerDefinition {
            val modelData = MeshDefinition()
            val modelPartData = modelData.root
            val body =
                modelPartData.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0f, 10.0f, 2.75f))

            val lower_body = body.addOrReplaceChild(
                "lower_body",
                CubeListBuilder.create().texOffs(46, 65).addBox(-5.5f, -8.0f, -2.5f, 11.0f, 8.0f, 5.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.0f, 1.5f, 0.0f, 0.0436f, 0.0f, 0.0f)
            )

            val upper_body = lower_body.addOrReplaceChild(
                "upper_body",
                CubeListBuilder.create().texOffs(42, 50).addBox(-6.5f, -7.5f, -4.0f, 13.0f, 8.0f, 7.0f, CubeDeformation(0.0f))
                    .texOffs(42, 34).addBox(-6.5f, -7.5f, -4.0f, 13.0f, 9.0f, 7.0f, CubeDeformation(0.25f)),
                PartPose.offsetAndRotation(0.0f, -7.0f, 0.0f, 0.3054f, 0.0f, 0.0f)
            )

            val left_arm =
                upper_body.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(6.75f, -6.25f, 0.15f))

            val upper_left_arm = left_arm.addOrReplaceChild(
                "upper_left_arm",
                CubeListBuilder.create().texOffs(83, 52)
                    .addBox(-0.5198f, -0.9918f, -2.7492f, 4.0f, 8.0f, 5.0f, CubeDeformation(0.0f))
                    .texOffs(83, 38).addBox(-0.5198f, -0.9918f, -2.7492f, 4.0f, 9.0f, 5.0f, CubeDeformation(0.25f)),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, -0.0869f, -0.0076f, -0.0869f)
            )

            val lower_left_arm = upper_left_arm.addOrReplaceChild(
                "lower_left_arm",
                CubeListBuilder.create().texOffs(85, 65).addBox(-1.5f, 0.0f, -4.0f, 3.0f, 10.0f, 4.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(1.2302f, 7.0082f, 2.2508f, -0.7854f, 0.0f, 0.0f)
            )

            val right_arm =
                upper_body.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-6.75f, -6.25f, 0.15f))

            val upper_right_arm = right_arm.addOrReplaceChild(
                "upper_right_arm",
                CubeListBuilder.create().texOffs(23, 52)
                    .addBox(-3.4802f, -0.9918f, -2.7492f, 4.0f, 8.0f, 5.0f, CubeDeformation(0.0f))
                    .texOffs(23, 38).addBox(-3.4802f, -0.9918f, -2.7492f, 4.0f, 9.0f, 5.0f, CubeDeformation(0.25f)),
                PartPose.offsetAndRotation(0.0f, 0.0f, 0.0f, -0.0869f, 0.0076f, 0.0869f)
            )

            val lower_right_arm = upper_right_arm.addOrReplaceChild(
                "lower_right_arm",
                CubeListBuilder.create().texOffs(25, 65).addBox(-1.5f, 0.0f, -4.0f, 3.0f, 10.0f, 4.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(-1.2302f, 7.0082f, 2.2508f, -0.7854f, 0.0f, 0.0f)
            )

            val head = upper_body.addOrReplaceChild(
                "head",
                CubeListBuilder.create().texOffs(46, 18).addBox(-4.0f, -5.5f, -4.0f, 8.0f, 8.0f, 8.0f, CubeDeformation(0.0f))
                    .texOffs(46, 0).addBox(-4.0f, -5.5f, -4.0f, 8.0f, 10.0f, 8.0f, CubeDeformation(0.25f)),
                PartPose.offsetAndRotation(0.0f, -7.607f, -1.9999f, -0.3054f, 0.0f, 0.0f)
            )

            val left_leg =
                lower_body.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(3.5f, -0.5f, 0.0f))

            val upper_left_leg = left_leg.addOrReplaceChild(
                "upper_left_leg",
                CubeListBuilder.create().texOffs(63, 93).addBox(-2.25f, -1.75f, -3.25f, 5.0f, 9.0f, 5.0f, CubeDeformation(0.0f))
                    .texOffs(63, 79).addBox(-2.25f, -1.75f, -3.25f, 5.0f, 9.0f, 5.0f, CubeDeformation(0.25f)),
                PartPose.offsetAndRotation(-0.5f, 1.0f, 0.0f, -0.6526f, -0.0617f, -0.0618f)
            )

            val lower_left_leg = upper_left_leg.addOrReplaceChild(
                "lower_left_leg",
                CubeListBuilder.create().texOffs(66, 107)
                    .addBox(-2.0f, -0.2358f, -0.039f, 4.0f, 10.0f, 4.0f, CubeDeformation(0.0f)),
                PartPose.offsetAndRotation(0.25f, 7.0f, -3.0f, 0.8727f, 0.0f, 0.0f)
            )

            val right_leg =
                lower_body.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-3.5f, -0.5f, 0.0f))

            val upper_right_leg = right_leg.addOrReplaceChild(
                "upper_right_leg",
                CubeListBuilder.create().texOffs(41, 93).mirror()
                    .addBox(-2.75f, -1.75f, -3.25f, 5.0f, 9.0f, 5.0f, CubeDeformation(0.0f)).mirror(false)
                    .texOffs(41, 79).mirror().addBox(-2.75f, -1.75f, -3.25f, 5.0f, 9.0f, 5.0f, CubeDeformation(0.25f))
                    .mirror(false),
                PartPose.offsetAndRotation(0.5f, 1.0f, 0.0f, -0.6526f, 0.0617f, 0.0618f)
            )

            val lower_right_leg = upper_right_leg.addOrReplaceChild(
                "lower_right_leg",
                CubeListBuilder.create().texOffs(43, 107).mirror()
                    .addBox(-1.75f, -0.2358f, -0.039f, 4.0f, 10.0f, 4.0f, CubeDeformation(0.0f)).mirror(false),
                PartPose.offsetAndRotation(-0.5f, 7.0f, -3.0f, 0.8727f, 0.0f, 0.0f)
            )
            return LayerDefinition.create(modelData, 128, 128)
        }
    }

    override fun setupAnim(
        renderState: SwampGolemRenderState,
    ) {
        super.setupAnim(renderState)

        slamAnimation.apply(renderState.slamAnimationState, renderState.ageInTicks)
        idleAnimation.apply(renderState.idleAnimationState, renderState.ageInTicks)
        walkingAnimation.apply(renderState.walkAnimationState, renderState.ageInTicks)

        setHeadAngles(renderState)
    }

    private fun setHeadAngles(
        renderState: SwampGolemRenderState,
    ) {
        head.xRot += Math.toRadians(renderState.xRot.toDouble()).toFloat()
        head.yRot += Math.toRadians(renderState.yRot.toDouble()).toFloat()
        head.yRot = Math.clamp(head.yRot, -0.8F, 0.8F)
    }
}