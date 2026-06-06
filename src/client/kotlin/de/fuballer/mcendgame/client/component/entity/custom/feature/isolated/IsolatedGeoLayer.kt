package de.fuballer.mcendgame.client.component.entity.custom.feature.isolated

import com.geckolib.animatable.GeoAnimatable
import com.geckolib.renderer.base.GeoRenderState
import com.geckolib.renderer.base.GeoRenderer
import com.geckolib.renderer.base.RenderPassInfo
import com.geckolib.renderer.layer.GeoRenderLayer
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState

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