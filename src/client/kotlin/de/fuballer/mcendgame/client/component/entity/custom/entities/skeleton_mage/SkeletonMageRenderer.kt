package de.fuballer.mcendgame.client.component.entity.custom.entities.skeleton_mage

import com.geckolib.constant.DataTickets
import com.geckolib.renderer.GeoEntityRenderer
import com.geckolib.renderer.base.BoneSnapshots
import com.geckolib.renderer.base.GeoRenderState
import com.geckolib.renderer.base.RenderPassInfo
import de.fuballer.mcendgame.client.component.entity.custom.feature.isolated.IsolatedGeoLayer
import de.fuballer.mcendgame.main.component.entity.custom.entities.skeleton_mage.SkeletonMageEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.HumanoidMobRenderer
import net.minecraft.client.renderer.entity.state.HumanoidRenderState
import kotlin.math.PI

class SkeletonMageRenderer<R>(
    context: EntityRendererProvider.Context
) : GeoEntityRenderer<SkeletonMageEntity, R>(context, SkeletonMageModel()) where R : HumanoidRenderState, R : GeoRenderState {
    init {
        withRenderLayer(IsolatedGeoLayer(this))
        withRenderLayer(SkeletonMageArmorLayer(this, context))
    }

    @Suppress("UNCHECKED_CAST")
    override fun createRenderState(animatable: SkeletonMageEntity, relatedObject: Void?): R = HumanoidRenderState() as R

    override fun extractRenderState(entity: SkeletonMageEntity, state: R, partialTick: Float) {
        super.extractRenderState(entity, state, partialTick)

        HumanoidMobRenderer.extractHumanoidRenderState(entity, state, partialTick, this.itemModelResolver)
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
    }
}