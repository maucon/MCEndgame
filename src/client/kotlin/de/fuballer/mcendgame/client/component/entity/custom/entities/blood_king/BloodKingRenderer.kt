package de.fuballer.mcendgame.client.component.entity.custom.entities.blood_king

import com.geckolib.constant.DataTickets
import com.geckolib.renderer.GeoEntityRenderer
import com.geckolib.renderer.base.BoneSnapshots
import com.geckolib.renderer.base.GeoRenderState
import com.geckolib.renderer.base.RenderPassInfo
import de.fuballer.mcendgame.main.component.entity.custom.entities.blood_king.BloodKingEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import kotlin.math.PI

class BloodKingRenderer<R>(
    context: EntityRendererProvider.Context
) : GeoEntityRenderer<BloodKingEntity, R>(context, BloodKingModel()) where R : LivingEntityRenderState, R : GeoRenderState {

    override fun adjustModelBonesForRender(renderPassInfo: RenderPassInfo<R>, snapshots: BoneSnapshots) {
        super.adjustModelBonesForRender(renderPassInfo, snapshots)

        snapshots.get("head").ifPresent {
            var pitch = renderPassInfo.getGeckolibData(DataTickets.ENTITY_PITCH)!!
            pitch = Math.clamp(pitch, -35F, 35F)
            it.rotX += -pitch * PI.toFloat() / 180F

            var yaw = renderPassInfo.getGeckolibData(DataTickets.ENTITY_YAW)!!
            yaw = Math.clamp(yaw, -45F, 45F)
            it.rotY += -yaw * PI.toFloat() / 180F
        }
    }
}