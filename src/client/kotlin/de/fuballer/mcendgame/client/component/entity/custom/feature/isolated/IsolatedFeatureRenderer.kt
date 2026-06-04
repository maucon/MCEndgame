package de.fuballer.mcendgame.client.component.entity.custom.feature.isolated

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.model.EntityModel
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState

class IsolatedFeatureRenderer<T : LivingEntityRenderState, M : EntityModel<T>>(
    featureContext: RenderLayerParent<T, M>,
) : RenderLayer<T, M>(featureContext) {
    override fun submit(
        matrices: PoseStack,
        queue: SubmitNodeCollector,
        light: Int,
        state: T,
        limbAngle: Float,
        limbDistance: Float,
    ) {
        IsolatedIndicatorRenderer.tryRender(state, matrices, queue, light)
    }
}