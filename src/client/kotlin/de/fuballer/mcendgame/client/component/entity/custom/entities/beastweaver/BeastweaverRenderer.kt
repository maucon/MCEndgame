package de.fuballer.mcendgame.client.component.entity.custom.entities.beastweaver

import com.geckolib.constant.DataTickets
import com.geckolib.constant.dataticket.DataTicket
import com.geckolib.renderer.GeoEntityRenderer
import com.geckolib.renderer.base.BoneSnapshots
import com.geckolib.renderer.base.GeoRenderState
import com.geckolib.renderer.base.RenderPassInfo
import com.google.common.reflect.TypeToken
import de.fuballer.mcendgame.client.component.entity.custom.feature.isolated.IsolatedGeoLayer
import de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver.BeastweaverEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import kotlin.math.PI

private val HIDDEN_BONES = DataTicket.create("hidden_bones", object : TypeToken<Set<String>>() {})

class BeastweaverRenderer<R>(
    context: EntityRendererProvider.Context
) : GeoEntityRenderer<BeastweaverEntity, R>(context, BeastweaverModel()) where R : LivingEntityRenderState, R : GeoRenderState {
    init {
        withRenderLayer(IsolatedGeoLayer(this))
    }

    override fun addRenderData(animatable: BeastweaverEntity, relatedObject: Void?, renderState: R, partialTick: Float) {
        super.addRenderData(animatable, relatedObject, renderState, partialTick)

        renderState.addGeckolibData(HIDDEN_BONES, animatable.getHiddenBones())
    }

    override fun adjustModelBonesForRender(renderPassInfo: RenderPassInfo<R>, snapshots: BoneSnapshots) {
        super.adjustModelBonesForRender(renderPassInfo, snapshots)

        snapshots.get("head").ifPresent {
            var pitch = renderPassInfo.getGeckolibData(DataTickets.ENTITY_PITCH)!!
            pitch = Math.clamp(pitch, -35F, 35F)
            it.rotX = -pitch * PI.toFloat() / 180F

            var yaw = renderPassInfo.getGeckolibData(DataTickets.ENTITY_YAW)!!
            yaw = Math.clamp(yaw, -45F, 45F)
            it.rotY = -yaw * PI.toFloat() / 180F
        }

        val toHide = renderPassInfo.getGeckolibData(HIDDEN_BONES) ?: return
        toHide.forEach { name ->
            snapshots.get(name).ifPresent {
                it.skipRender(true)
                it.skipChildrenRender(true)
            }
        }
    }
}