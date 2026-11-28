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
import kotlin.math.cos
import kotlin.math.sin

class MagmaSpikeRenderer<R>(
    context: EntityRendererProvider.Context
) : GeoEntityRenderer<MagmaSpikeEntity, R>(context, MagmaSpikeModel()) where R : EntityRenderState, R : GeoRenderState {
    companion object {
        private val EMERGED_PERCENTAGE = DataTicket.create("emerged_percentage", object : TypeToken<Float>() {})
        private val XZ_ROT_RAD = DataTicket.create("xr_rot_rad", object : TypeToken<Float>() {})
        private val SCALE = DataTicket.create("scale", object : TypeToken<Float>() {})
    }

    override fun addRenderData(animatable: MagmaSpikeEntity, relatedObject: Void?, renderState: R, partialTick: Float) {
        renderState.addGeckolibData(EMERGED_PERCENTAGE, animatable.getEmergedPercentage(partialTick))
        renderState.addGeckolibData(XZ_ROT_RAD, animatable.entityData.get(MagmaSpikeEntity.DATA_XZ_ROT_RAD))
        renderState.addGeckolibData(SCALE, animatable.entityData.get(MagmaSpikeEntity.DATA_SCALE))
    }

    override fun adjustModelBonesForRender(renderPassInfo: RenderPassInfo<R>, snapshots: BoneSnapshots) {
        super.adjustModelBonesForRender(renderPassInfo, snapshots)

        snapshots.get("magma_spike").ifPresent {
            val angle = renderPassInfo.getGeckolibData(XZ_ROT_RAD) ?: 0.0f
            it.rotX = -angle

            val emergedPercentage = renderPassInfo.getGeckolibData(EMERGED_PERCENTAGE) ?: 0.0f
            val offset = -14f * (1.0f - emergedPercentage)
            it.translateZ -= offset * sin(angle)
            it.translateY += offset * cos(angle)
        }
    }

    override fun scaleModelForRender(
        renderPassInfo: RenderPassInfo<R>,
        widthScale: Float,
        heightScale: Float
    ) {
        val scale = renderPassInfo.getGeckolibData(SCALE) ?: 1.0f
        super.scaleModelForRender(renderPassInfo, (widthScale * scale), (heightScale * scale))
    }
}