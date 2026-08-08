package de.fuballer.mcendgame.client.component.entity.custom.entities.beastweaver.beastweaver_vine

import com.geckolib.renderer.GeoEntityRenderer
import com.geckolib.renderer.base.BoneSnapshots
import com.geckolib.renderer.base.GeoRenderState
import com.geckolib.renderer.base.RenderPassInfo
import de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver.beastweaver_vine.BeastweaverVineEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState

class BeastweaverVineRenderer<R>(
    context: EntityRendererProvider.Context
) : GeoEntityRenderer<BeastweaverVineEntity, R>(context, BeastweaverVineModel()) where R : LivingEntityRenderState, R : GeoRenderState {
    override fun adjustModelBonesForRender(renderPassInfo: RenderPassInfo<R>, snapshots: BoneSnapshots) {
        super.adjustModelBonesForRender(renderPassInfo, snapshots)

        // TODO add rotations
    }
}