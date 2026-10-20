package de.fuballer.mcendgame.client.component.entity.custom.entities.magma_spike

import com.geckolib.constant.dataticket.DataTicket
import com.geckolib.renderer.GeoEntityRenderer
import com.geckolib.renderer.base.BoneSnapshots
import com.geckolib.renderer.base.GeoRenderState
import com.geckolib.renderer.base.RenderPassInfo
import com.google.common.reflect.TypeToken
import de.fuballer.mcendgame.main.component.entity.custom.entities.magma_spike.MagmaSpikeEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.state.EntityRenderState

class MagmaSpikeRenderer<R>(
    context: EntityRendererProvider.Context
) : GeoEntityRenderer<MagmaSpikeEntity, R>(context, MagmaSpikeModel()) where R : EntityRenderState, R : GeoRenderState {
    companion object {
        private val ANIMATION_PROGRESS = DataTicket.create("animation_progress", object : TypeToken<Float>() {})
    }

    override fun addRenderData(animatable: MagmaSpikeEntity, relatedObject: Void?, renderState: R, partialTick: Float) {
        renderState.addGeckolibData(ANIMATION_PROGRESS, animatable.getAnimationProgress(partialTick))
    }

    override fun adjustModelBonesForRender(renderPassInfo: RenderPassInfo<R>, snapshots: BoneSnapshots) {
        super.adjustModelBonesForRender(renderPassInfo, snapshots)

        snapshots.get("magma_spike").ifPresent {
            val progress = renderPassInfo.getGeckolibData(ANIMATION_PROGRESS) ?: 0.0f
            it.translateY = -14f * (1.0f - progress)
        }
    }
}