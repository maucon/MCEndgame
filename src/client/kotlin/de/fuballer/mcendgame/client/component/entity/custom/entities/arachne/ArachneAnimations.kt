package de.fuballer.mcendgame.client.component.entity.custom.entities.arachne

import net.minecraft.client.animation.AnimationChannel
import net.minecraft.client.animation.AnimationDefinition
import net.minecraft.client.animation.Keyframe
import net.minecraft.client.animation.KeyframeAnimations

object ArachneAnimations {
    val WALKING: AnimationDefinition = AnimationDefinition.Builder.withLength(1.1667f).looping()
        .addAnimation(
            "temurLeft1", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -5.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -5.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5417f, KeyframeAnimations.degreeVec(0.0111f, 12.4995f, 47.6074f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -5.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tibiaLeft1", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 5.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.2083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -17.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5417f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -57.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 5.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "metatarsusLeft1", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 5.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.2083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -12.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5417f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -25.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 5.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "temurLeft2", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(-8.4f, 15.5065f, 26.0154f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.2917f, KeyframeAnimations.degreeVec(6.3941f, 6.9262f, 11.4667f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.625f, KeyframeAnimations.degreeVec(22.28f, -2.9f, 6.9f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.7917f, KeyframeAnimations.degreeVec(19.9761f, -1.0844f, 2.2559f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(-8.44f, 15.5f, 26.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tibiaLeft2", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -30.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.2917f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -11.25f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.625f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -7.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.7917f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -12.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -30.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "metatarsusLeft2", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -20.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.2917f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -10.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.625f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -5.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.7917f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -7.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -20.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "temurLeft3", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(21.35f, -10.3f, 4.9f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.1667f, KeyframeAnimations.degreeVec(22.1411f, -8.4073f, 0.2048f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5833f, KeyframeAnimations.degreeVec(-12.537f, 4.9106f, -0.9356f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.875f, KeyframeAnimations.degreeVec(4.6517f, -2.2574f, -3.0047f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(21.35f, -10.3f, 4.9f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tibiaLeft3", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -10.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -15.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5833f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.875f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -10.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "metatarsusLeft3", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -2.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -5.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5833f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.875f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 1.25f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -2.5f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "temurLeft4", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(-13.1323f, 2.6954f, -11.041f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.2917f, KeyframeAnimations.degreeVec(0.7077f, -7.4856f, -0.7504f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5417f, KeyframeAnimations.degreeVec(11.7765f, -17.8414f, 29.73f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.7083f, KeyframeAnimations.degreeVec(7.5044f, -7.5659f, 19.1435f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(-13.1323f, 2.6954f, -11.041f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tibiaLeft4", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 12.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.2917f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 2.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5417f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -32.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.7083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -32.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 12.5f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "metatarsusLeft4", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 2.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.2917f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -3.75f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5417f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -25.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.7083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -22.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 2.5f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "temurRight1", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, -12.5f, -47.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5833f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 7.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.75f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 7.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, -12.5f, -47.5f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tibiaRight1", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 57.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5833f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -7.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.7917f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 15.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 57.5f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "metatarsusRight1", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 25.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5833f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -5.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.7917f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 10.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 25.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "temurRight2", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(22.2826f, 2.9191f, -6.9128f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.1667f, KeyframeAnimations.degreeVec(19.9701f, 1.077f, -2.2617f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.625f, KeyframeAnimations.degreeVec(-8.4406f, -15.5092f, -25.976f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.9167f, KeyframeAnimations.degreeVec(6.3759f, -6.9163f, -11.4533f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(22.2826f, 2.9191f, -6.9128f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tibiaRight2", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 7.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 12.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.625f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 30.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.9167f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 11.25f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 7.5f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "metatarsusRight2", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 5.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 7.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.625f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 20.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.9167f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 10.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 5.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "temurRight3", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(-12.537f, -4.9106f, 0.9356f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.2917f, KeyframeAnimations.degreeVec(4.6591f, 2.3022f, 3.007f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5417f, KeyframeAnimations.degreeVec(21.3599f, 10.3963f, -4.9009f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.7083f, KeyframeAnimations.degreeVec(22.1589f, 8.5024f, -0.2048f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(-12.537f, -4.9106f, 0.9356f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tibiaRight3", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.2917f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5417f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 10.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.7083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 15.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "metatarsusRight3", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.2917f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -1.25f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5417f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 2.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.7083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 7.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "temurRight4", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(11.77f, 17.8f, -29.7f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.1667f, KeyframeAnimations.degreeVec(12.5975f, 9.9351f, -18.4357f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5417f, KeyframeAnimations.degreeVec(-13.1f, -2.7f, 11.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.8333f, KeyframeAnimations.degreeVec(0.7f, 7.5f, 0.75f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(11.77f, 17.8f, -29.7f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tibiaRight4", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 32.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 32.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5417f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -12.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.8333f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -2.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 32.5f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "metatarsusRight4", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 25.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 22.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5417f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -2.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.8333f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 3.75f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 25.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "upperbody", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.5f, -1.0f, -0.3f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.2917f, KeyframeAnimations.degreeVec(-0.2f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5833f, KeyframeAnimations.degreeVec(0.5f, 1.0f, 0.3f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.875f, KeyframeAnimations.degreeVec(-0.2f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.5f, -1.0f, -0.3f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "chest", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.5f, -1.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.2917f, KeyframeAnimations.degreeVec(-0.2f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5833f, KeyframeAnimations.degreeVec(0.5f, 1.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.875f, KeyframeAnimations.degreeVec(-0.2f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.5f, -1.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "head", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 1.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5833f, KeyframeAnimations.degreeVec(0.0f, -1.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 1.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "armLeft", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(2.5f, 0.0f, -2.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.625f, KeyframeAnimations.degreeVec(-2.5f, 0.0f, -2.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(2.5f, 0.0f, -2.5f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "armRight", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(-2.5f, 0.0f, 2.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5417f, KeyframeAnimations.degreeVec(2.5f, 0.0f, 2.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(-2.5f, 0.0f, 2.5f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "abdomen", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(1.5f, -1.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.2917f, KeyframeAnimations.degreeVec(-1.5f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5833f, KeyframeAnimations.degreeVec(1.5f, 1.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.875f, KeyframeAnimations.degreeVec(-1.5f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(1.5f, -1.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .build()

    val IDLE: AnimationDefinition = AnimationDefinition.Builder.withLength(4.0f).looping()
        .addAnimation(
            "cephalothorax", AnimationChannel(
                AnimationChannel.Targets.POSITION,
                Keyframe(0.0f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.9583f, KeyframeAnimations.posVec(0.0f, -0.5f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(4.0f, KeyframeAnimations.posVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "temurLeft1", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(2.2083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -2.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(4.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tibiaLeft1", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(2.2083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 1.3f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(4.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "metatarsusLeft1", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(2.2083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(4.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "temurLeft2", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(2.0417f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -2.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(4.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tibiaLeft2", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(2.0417f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 1.3f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(4.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "metatarsusLeft2", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(2.0417f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(4.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "temurLeft3", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.875f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -2.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(4.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tibiaLeft3", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.875f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 1.3f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(4.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "metatarsusLeft3", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.875f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(4.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "temurLeft4", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(2.0833f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -2.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(4.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tibiaLeft4", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(2.0833f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 1.3f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(4.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "metatarsusLeft4", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(2.0833f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(4.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "temurRight1", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(2.0417f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 2.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(4.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tibiaRight1", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(2.0417f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -1.3f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(4.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "metatarsusRight1", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(2.0417f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -0.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(4.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "temurRight2", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(2.125f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 2.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(4.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tibiaRight2", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(2.125f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -1.3f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(4.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "metatarsusRight2", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(2.125f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -0.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(4.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "temurRight3", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(2.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 2.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(4.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tibiaRight3", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(2.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -1.3f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(4.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "metatarsusRight3", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(2.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -0.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(4.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "temurRight4", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.8333f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 2.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(4.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tibiaRight4", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.8333f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -1.3f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(4.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "metatarsusRight4", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.8333f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -0.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(4.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "upperbody", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(2.0f, KeyframeAnimations.degreeVec(-1.5f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(4.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "neck", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(2.125f, KeyframeAnimations.degreeVec(1.5f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(4.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "armLeft", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -5.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(2.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -2.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(4.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -5.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "armRight", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 5.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.9167f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 2.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(4.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 5.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "abdomen", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.9167f, KeyframeAnimations.degreeVec(1.5f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(4.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .build()

    val WALKING_BACKWARDS: AnimationDefinition = AnimationDefinition.Builder.withLength(1.1667f).looping()
        .addAnimation(
            "temurLeft1", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -5.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.625f, KeyframeAnimations.degreeVec(0.0111f, 12.4995f, 47.6074f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -5.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -5.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tibiaLeft1", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 5.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.625f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -57.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.9583f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -17.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 5.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "metatarsusLeft1", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 5.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.625f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -25.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.9583f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -12.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 5.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "temurLeft2", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(-8.44f, 15.5f, 26.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.375f, KeyframeAnimations.degreeVec(19.9761f, -1.0844f, 2.2559f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5417f, KeyframeAnimations.degreeVec(22.28f, -2.9f, 6.9f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.875f, KeyframeAnimations.degreeVec(6.3941f, 6.9262f, 11.4667f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(-8.4f, 15.5065f, 26.0154f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tibiaLeft2", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -30.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.375f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -12.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5417f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -7.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.875f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -11.25f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -30.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "metatarsusLeft2", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -20.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.375f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -7.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5417f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -5.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.875f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -10.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -20.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "temurLeft3", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(21.35f, -10.3f, 4.9f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.2917f, KeyframeAnimations.degreeVec(4.6517f, -2.2574f, -3.0047f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5833f, KeyframeAnimations.degreeVec(-12.537f, 4.9106f, -0.9356f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.0f, KeyframeAnimations.degreeVec(22.1411f, -8.4073f, 0.2048f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(21.35f, -10.3f, 4.9f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tibiaLeft3", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -10.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.2917f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5833f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -15.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -10.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "metatarsusLeft3", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -2.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.2917f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 1.25f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5833f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -5.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -2.5f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "temurLeft4", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(-13.1323f, 2.6954f, -11.041f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.4583f, KeyframeAnimations.degreeVec(7.5044f, -7.5659f, 19.1435f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.625f, KeyframeAnimations.degreeVec(11.7765f, -17.8414f, 29.73f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.875f, KeyframeAnimations.degreeVec(0.7077f, -7.4856f, -0.7504f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(-13.1323f, 2.6954f, -11.041f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tibiaLeft4", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 12.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.4583f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -32.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.625f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -32.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.875f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 2.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 12.5f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "metatarsusLeft4", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 2.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.4583f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -22.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.625f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -25.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.875f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -3.75f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 2.5f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "temurRight1", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, -12.5f, -47.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.4167f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 7.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5833f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 7.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, -12.5f, -47.5f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tibiaRight1", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 57.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.375f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 15.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5833f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -7.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 57.5f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "metatarsusRight1", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 25.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.375f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 10.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5833f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -5.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 25.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "temurRight2", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(22.2826f, 2.9191f, -6.9128f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.25f, KeyframeAnimations.degreeVec(6.3759f, -6.9163f, -11.4533f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5417f, KeyframeAnimations.degreeVec(-8.4406f, -15.5092f, -25.976f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.0f, KeyframeAnimations.degreeVec(19.9701f, 1.077f, -2.2617f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(22.2826f, 2.9191f, -6.9128f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tibiaRight2", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 7.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.25f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 11.25f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5417f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 30.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 12.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 7.5f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "metatarsusRight2", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 5.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.25f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 10.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5417f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 20.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 7.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 5.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "temurRight3", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(-12.537f, -4.9106f, 0.9356f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.4583f, KeyframeAnimations.degreeVec(22.1589f, 8.5024f, -0.2048f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.625f, KeyframeAnimations.degreeVec(21.3599f, 10.3963f, -4.9009f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.875f, KeyframeAnimations.degreeVec(4.6591f, 2.3022f, 3.007f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(-12.537f, -4.9106f, 0.9356f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tibiaRight3", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.4583f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 15.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.625f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 10.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.875f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "metatarsusRight3", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.4583f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 7.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.625f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 2.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.875f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -1.25f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "temurRight4", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(11.77f, 17.8f, -29.7f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.3333f, KeyframeAnimations.degreeVec(0.7f, 7.5f, 0.75f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.625f, KeyframeAnimations.degreeVec(-13.1f, -2.7f, 11.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.0f, KeyframeAnimations.degreeVec(12.5975f, 9.9351f, -18.4357f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(11.77f, 17.8f, -29.7f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tibiaRight4", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 32.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.3333f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -2.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.625f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -12.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 32.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 32.5f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "metatarsusRight4", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 25.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.3333f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 3.75f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.625f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -2.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 22.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 25.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "upperbody", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.5f, -1.0f, -0.3f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.2917f, KeyframeAnimations.degreeVec(-0.2f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5833f, KeyframeAnimations.degreeVec(0.5f, 1.0f, 0.3f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.875f, KeyframeAnimations.degreeVec(-0.2f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.5f, -1.0f, -0.3f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "chest", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.5f, -1.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.2917f, KeyframeAnimations.degreeVec(-0.2f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5833f, KeyframeAnimations.degreeVec(0.5f, 1.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.875f, KeyframeAnimations.degreeVec(-0.2f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.5f, -1.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "head", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 1.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5833f, KeyframeAnimations.degreeVec(0.0f, -1.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(0.0f, 1.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "armLeft", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(2.5f, 0.0f, -2.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5417f, KeyframeAnimations.degreeVec(-2.5f, 0.0f, -2.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(2.5f, 0.0f, -2.5f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "armRight", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(-2.5f, 0.0f, 2.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.625f, KeyframeAnimations.degreeVec(2.5f, 0.0f, 2.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(-2.5f, 0.0f, 2.5f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "abdomen", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(1.5f, -1.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.2917f, KeyframeAnimations.degreeVec(-1.5f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.5833f, KeyframeAnimations.degreeVec(1.5f, 1.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.875f, KeyframeAnimations.degreeVec(-1.5f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(1.1667f, KeyframeAnimations.degreeVec(1.5f, -1.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .build()

    val SPIT: AnimationDefinition = AnimationDefinition.Builder.withLength(0.4167f)
        .addAnimation(
            "upperbody", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.1667f, KeyframeAnimations.degreeVec(25.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.25f, KeyframeAnimations.degreeVec(25.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.4167f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "chest", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.1667f, KeyframeAnimations.degreeVec(5.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.25f, KeyframeAnimations.degreeVec(5.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.4167f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "neck", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.1667f, KeyframeAnimations.degreeVec(-15.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.25f, KeyframeAnimations.degreeVec(-15.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.4167f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "head", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.1667f, KeyframeAnimations.degreeVec(-10.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.25f, KeyframeAnimations.degreeVec(-10.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.4167f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "armLeft", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.1667f, KeyframeAnimations.degreeVec(29.2259f, 6.3347f, -5.2292f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.25f, KeyframeAnimations.degreeVec(29.2259f, 6.3347f, -5.2292f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.4167f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "armRight", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.1667f, KeyframeAnimations.degreeVec(29.8774f, -1.936f, 7.2472f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.25f, KeyframeAnimations.degreeVec(29.8774f, -1.936f, 7.2472f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.4167f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .build()

    val ATTACK: AnimationDefinition = AnimationDefinition.Builder.withLength(0.7083f)
        .addAnimation(
            "temurLeft1", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.125f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -7.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.2917f, KeyframeAnimations.degreeVec(0.0f, 0.0f, -7.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.375f, KeyframeAnimations.degreeVec(-5.1582f, 23.5672f, 49.9395f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.7083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tibiaLeft1", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.1667f, KeyframeAnimations.degreeVec(-5.0f, 0.0f, -70.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.3333f, KeyframeAnimations.degreeVec(-5.0f, 0.0f, -70.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.4167f, KeyframeAnimations.degreeVec(-0.0872f, 0.9962f, -67.5008f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.7083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "metatarsusLeft1", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.1667f, KeyframeAnimations.degreeVec(-5.0f, 0.0f, -20.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.3333f, KeyframeAnimations.degreeVec(-5.0f, 0.0f, -20.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.4167f, KeyframeAnimations.degreeVec(2.1938f, 1.2202f, -32.4963f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.7083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tarsusLeft1", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.2083f, KeyframeAnimations.degreeVec(-5.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.3333f, KeyframeAnimations.degreeVec(-5.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.4167f, KeyframeAnimations.degreeVec(-5.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.7083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "temurRight1", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.125f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 7.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.2917f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 7.5f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.375f, KeyframeAnimations.degreeVec(-5.1542f, -23.5682f, -49.9457f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.7083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tibiaRight1", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.1667f, KeyframeAnimations.degreeVec(-5.0f, 0.0f, 70.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.3333f, KeyframeAnimations.degreeVec(-5.0f, 0.0f, 70.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.4167f, KeyframeAnimations.degreeVec(-0.0716f, -1.0849f, 67.5479f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.7083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "metatarsusRight1", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.2083f, KeyframeAnimations.degreeVec(-5.0f, 0.0f, 20.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.3333f, KeyframeAnimations.degreeVec(-5.0f, 0.0f, 20.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.4167f, KeyframeAnimations.degreeVec(2.242f, -1.2295f, 32.5001f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.7083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "tarsusRight1", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.2083f, KeyframeAnimations.degreeVec(-5.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.3333f, KeyframeAnimations.degreeVec(-5.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.4167f, KeyframeAnimations.degreeVec(-5.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.7083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "upperbody", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.1667f, KeyframeAnimations.degreeVec(-7.5f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.3333f, KeyframeAnimations.degreeVec(-7.5f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.4167f, KeyframeAnimations.degreeVec(10.68f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.7083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "chest", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.2083f, KeyframeAnimations.degreeVec(-5.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.3333f, KeyframeAnimations.degreeVec(-5.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.4167f, KeyframeAnimations.degreeVec(10.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.7083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "neck", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.1667f, KeyframeAnimations.degreeVec(5.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.3333f, KeyframeAnimations.degreeVec(5.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.4167f, KeyframeAnimations.degreeVec(-10.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.7083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "head", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.1667f, KeyframeAnimations.degreeVec(2.5f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.3333f, KeyframeAnimations.degreeVec(2.5f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.4167f, KeyframeAnimations.degreeVec(-7.5f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.7083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "armLeft", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.2083f, KeyframeAnimations.degreeVec(-5.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.375f, KeyframeAnimations.degreeVec(-5.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.4583f, KeyframeAnimations.degreeVec(15.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.7083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "armRight", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.2083f, KeyframeAnimations.degreeVec(-5.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.375f, KeyframeAnimations.degreeVec(-5.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.4583f, KeyframeAnimations.degreeVec(15.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.7083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .addAnimation(
            "abdomen", AnimationChannel(
                AnimationChannel.Targets.ROTATION,
                Keyframe(0.0f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.1667f, KeyframeAnimations.degreeVec(-7.5f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.2917f, KeyframeAnimations.degreeVec(-7.5f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.375f, KeyframeAnimations.degreeVec(7.5f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM),
                Keyframe(0.7083f, KeyframeAnimations.degreeVec(0.0f, 0.0f, 0.0f), AnimationChannel.Interpolations.CATMULLROM)
            )
        )
        .build()
}