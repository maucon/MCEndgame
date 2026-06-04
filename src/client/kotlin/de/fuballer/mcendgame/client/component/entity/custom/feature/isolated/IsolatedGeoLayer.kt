package de.fuballer.mcendgame.client.component.entity.custom.feature.isolated

import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import software.bernie.geckolib.animatable.GeoAnimatable
import software.bernie.geckolib.renderer.base.GeoRenderState
import software.bernie.geckolib.renderer.base.GeoRenderer
import software.bernie.geckolib.renderer.base.RenderPassInfo
import software.bernie.geckolib.renderer.layer.GeoRenderLayer

class IsolatedGeoLayer<T : GeoAnimatable, O : Any, R : GeoRenderState>(
    renderer: GeoRenderer<T, O, R>,
) : GeoRenderLayer<T, O, R>(renderer) {
    override fun submitRenderTask(renderPassInfo: RenderPassInfo<R>, renderTasks: SubmitNodeCollector) {
        super.submitRenderTask(renderPassInfo, renderTasks)

        val renderState = renderPassInfo.renderState()
        val livingEntityRenderState = renderState as? LivingEntityRenderState ?: return
        IsolatedIndicatorRenderer.tryRender(livingEntityRenderState, renderPassInfo.poseStack(), renderTasks, renderState.lightCoords, true)
    }
}