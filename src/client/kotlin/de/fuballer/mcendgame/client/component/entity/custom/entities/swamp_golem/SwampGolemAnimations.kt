package de.fuballer.mcendgame.client.component.entity.custom.entities.swamp_golem

import net.minecraft.client.animation.AnimationChannel
import net.minecraft.client.animation.AnimationDefinition
import net.minecraft.client.animation.Keyframe
import net.minecraft.client.animation.KeyframeAnimations

object SwampGolemAnimations {
    val SLAM: AnimationDefinition = AnimationDefinition.Builder.withLength(1.25f)
        .addAnimation(
            "lower_body", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    0.5f,
                    KeyframeAnimations.degreeVec(-5.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.8333f,
                    KeyframeAnimations.degreeVec(70.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    1.25f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                )
            )
        )
        .addAnimation(
            "lower_body", AnimationChannel(
                AnimationChannel.Targets.POSITION,
                Keyframe(
                    0.0f,
                    KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    0.5f,
                    KeyframeAnimations.posVec(0.0f, 1.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.8333f,
                    KeyframeAnimations.posVec(0.0f, -1.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    1.25f,
                    KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                )
            )
        )
        .addAnimation(
            "upper_body", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    0.5f,
                    KeyframeAnimations.degreeVec(-7.5093f, -0.434f, 2.4621f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.8333f,
                    KeyframeAnimations.degreeVec(24.9728f, 1.6887f, -1.8437f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    1.25f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                )
            )
        )
        .addAnimation(
            "upper_left_arm", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    0.5f,
                    KeyframeAnimations.degreeVec(-160.0179f, 1.2957f, -4.8293f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.8333f,
                    KeyframeAnimations.degreeVec(-137.4437f, 6.1247f, -3.5303f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    1.25f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                )
            )
        )
        .addAnimation(
            "lower_left_arm", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    0.5f,
                    KeyframeAnimations.degreeVec(25.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.8333f,
                    KeyframeAnimations.degreeVec(15.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    1.25f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                )
            )
        )
        .addAnimation(
            "right_arm", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    0.5f,
                    KeyframeAnimations.degreeVec(0.0f, -5.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.25f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                )
            )
        )
        .addAnimation(
            "upper_right_arm", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    0.5f,
                    KeyframeAnimations.degreeVec(-159.9828f, 3.5337f, 6.1251f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.8333f,
                    KeyframeAnimations.degreeVec(-137.5179f, -1.2957f, 4.8293f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    1.25f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                )
            )
        )
        .addAnimation(
            "lower_right_arm", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    0.5f,
                    KeyframeAnimations.degreeVec(25.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.8333f,
                    KeyframeAnimations.degreeVec(15.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    1.25f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                )
            )
        )
        .addAnimation(
            "head", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    0.5f,
                    KeyframeAnimations.degreeVec(7.5f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.8333f,
                    KeyframeAnimations.degreeVec(-15.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    1.25f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                )
            )
        )
        .addAnimation(
            "upper_left_leg", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    0.5f,
                    KeyframeAnimations.degreeVec(10.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    0.8333f,
                    KeyframeAnimations.degreeVec(-80.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    1.25f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                )
            )
        )
        .addAnimation(
            "upper_left_leg", AnimationChannel(
                AnimationChannel.Targets.POSITION,
                Keyframe(
                    0.0f,
                    KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    0.5f,
                    KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    0.8333f,
                    KeyframeAnimations.posVec(0.0f, 2.0f, -1.75f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    1.25f,
                    KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                )
            )
        )
        .addAnimation(
            "lower_left_leg", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    0.5f,
                    KeyframeAnimations.degreeVec(-10.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    0.8333f,
                    KeyframeAnimations.degreeVec(20.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    1.25f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                )
            )
        )
        .addAnimation(
            "right_leg", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    0.5f,
                    KeyframeAnimations.degreeVec(-2.5f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.25f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                )
            )
        )
        .build()

    val IDLE: AnimationDefinition = AnimationDefinition.Builder.withLength(3.0f).looping()
        .addAnimation(
            "upper_body", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.5f,
                    KeyframeAnimations.degreeVec(2.5f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    3.0f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "upper_left_arm", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.5833f,
                    KeyframeAnimations.degreeVec(-0.0064f, -2.4905f, -4.7827f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    3.0f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                )
            )
        )
        .addAnimation(
            "upper_right_arm", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.4167f,
                    KeyframeAnimations.degreeVec(-0.0064f, 2.4905f, 4.7827f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    2.9583f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                )
            )
        )
        .build()

    val WALKING: AnimationDefinition = AnimationDefinition.Builder.withLength(3.625f).looping()
        .addAnimation(
            "lower_body", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    0.9167f,
                    KeyframeAnimations.degreeVec(0.216f, -4.8862f, -2.5067f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.8333f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    2.7083f,
                    KeyframeAnimations.degreeVec(0.216f, 4.8862f, 2.5067f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    3.625f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                )
            )
        )
        .addAnimation(
            "lower_body", AnimationChannel(
                AnimationChannel.Targets.POSITION,
                Keyframe(
                    0.0f,
                    KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    0.5417f,
                    KeyframeAnimations.posVec(0.0f, -0.5f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    0.9167f,
                    KeyframeAnimations.posVec(0.0f, -1.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.8333f,
                    KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    2.375f,
                    KeyframeAnimations.posVec(0.0f, -0.5f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    2.7083f,
                    KeyframeAnimations.posVec(0.0f, -1.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    3.625f,
                    KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                )
            )
        )
        .addAnimation(
            "upper_body", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    0.9167f,
                    KeyframeAnimations.degreeVec(0.0f, 2.5f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.8333f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    2.7083f,
                    KeyframeAnimations.degreeVec(0.0f, -2.5f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    3.625f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                )
            )
        )
        .addAnimation(
            "upper_left_arm", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    0.9583f,
                    KeyframeAnimations.degreeVec(-12.5214f, -2.3844f, 0.7517f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.875f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    2.75f,
                    KeyframeAnimations.degreeVec(12.5f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    3.625f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                )
            )
        )
        .addAnimation(
            "lower_left_arm", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    0.9583f,
                    KeyframeAnimations.degreeVec(-5.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.875f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    2.75f,
                    KeyframeAnimations.degreeVec(5.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    3.625f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                )
            )
        )
        .addAnimation(
            "upper_right_arm", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    0.875f,
                    KeyframeAnimations.degreeVec(12.5f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.7917f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    2.6667f,
                    KeyframeAnimations.degreeVec(-12.5214f, 2.3844f, -0.7517f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    3.625f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                )
            )
        )
        .addAnimation(
            "lower_right_arm", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    0.875f,
                    KeyframeAnimations.degreeVec(5.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.7917f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    2.6667f,
                    KeyframeAnimations.degreeVec(-5.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    3.625f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                )
            )
        )
        .addAnimation(
            "head", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    1.0f,
                    KeyframeAnimations.degreeVec(-2.5f, 0.0f, 2.5f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.9167f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    2.7917f,
                    KeyframeAnimations.degreeVec(-2.5f, 0.0f, -2.5f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    3.625f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                )
            )
        )
        .addAnimation(
            "upper_left_leg", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    0.8333f,
                    KeyframeAnimations.degreeVec(15.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.75f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    2.625f,
                    KeyframeAnimations.degreeVec(-22.4211f, -2.3073f, -0.9659f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    3.625f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                )
            )
        )
        .addAnimation(
            "lower_left_leg", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    0.8333f,
                    KeyframeAnimations.degreeVec(20.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.75f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    2.625f,
                    KeyframeAnimations.degreeVec(-10.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    3.625f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                )
            )
        )
        .addAnimation(
            "upper_right_leg", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    0.9583f,
                    KeyframeAnimations.degreeVec(-22.4211f, 2.3073f, 0.9659f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.875f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    2.75f,
                    KeyframeAnimations.degreeVec(15.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    3.625f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                )
            )
        )
        .addAnimation(
            "lower_right_leg", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(
                    0.0f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    0.9583f,
                    KeyframeAnimations.degreeVec(-10.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    1.875f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                ),
                Keyframe(
                    2.75f,
                    KeyframeAnimations.degreeVec(20.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.CATMULLROM
                ),
                Keyframe(
                    3.625f,
                    KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f),
                    AnimationChannel.Interpolations.LINEAR
                )
            )
        )
        .build()
}