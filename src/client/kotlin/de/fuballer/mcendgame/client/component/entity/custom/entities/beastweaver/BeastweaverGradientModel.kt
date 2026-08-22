package de.fuballer.mcendgame.client.component.entity.custom.entities.beastweaver

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer

interface BeastweaverGradientModel {
    fun renderToBufferWithGradient(
        poseStack: PoseStack,
        buffer: VertexConsumer,
        lightCoords: Int,
        overlayCoords: Int,
        color: Int,
        gradientData: BeastweaverGradientData,
    )
}