package de.fuballer.mcendgame.client.component.entity.custom.entities.beastweaver.beastweaver_wolf

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import de.fuballer.mcendgame.client.accessor.ModelPartBeastweaverGradientAccessor
import de.fuballer.mcendgame.client.component.entity.custom.entities.beastweaver.BeastweaverGradientData
import de.fuballer.mcendgame.client.component.entity.custom.entities.beastweaver.BeastweaverGradientModel
import net.minecraft.client.model.animal.wolf.WolfModel
import net.minecraft.client.model.geom.ModelPart

class BeastweaverWolfModel(
    root: ModelPart,
) : WolfModel(root), BeastweaverGradientModel {
    override fun renderToBufferWithGradient(
        poseStack: PoseStack,
        buffer: VertexConsumer,
        lightCoords: Int,
        overlayCoords: Int,
        color: Int,
        gradientData: BeastweaverGradientData,
    ) {
        @Suppress("CAST_NEVER_SUCCEEDS") val accessor = root() as ModelPartBeastweaverGradientAccessor
        accessor.`mcendgame$renderWithGradient`(poseStack, buffer, lightCoords, overlayCoords, color, gradientData)
    }
}