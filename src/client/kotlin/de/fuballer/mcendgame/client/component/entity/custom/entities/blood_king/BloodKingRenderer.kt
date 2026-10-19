package de.fuballer.mcendgame.client.component.entity.custom.entities.blood_king

import com.geckolib.constant.DataTickets
import com.geckolib.constant.dataticket.DataTicket
import com.geckolib.renderer.GeoEntityRenderer
import com.geckolib.renderer.base.BoneSnapshots
import com.geckolib.renderer.base.GeoRenderState
import com.geckolib.renderer.base.RenderPassInfo
import com.google.common.reflect.TypeToken
import de.fuballer.mcendgame.main.component.entity.custom.GeoEntityLocatorService
import de.fuballer.mcendgame.main.component.entity.custom.entities.blood_king.BloodKingEntity
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import java.util.*
import kotlin.math.PI

class BloodKingRenderer<R>(
    context: EntityRendererProvider.Context
) : GeoEntityRenderer<BloodKingEntity, R>(context, BloodKingModel()) where R : LivingEntityRenderState, R : GeoRenderState {
    companion object {
        private val LOCATORS = listOf(
            "swordParticle0",
            "swordParticle1",
            "swordUltimateParticle1",
        )

        private val UUID_TICKET = DataTicket.create("uuid", object : TypeToken<UUID>() {})
    }

    init {
        shadowRadius = 0.65f
    }

    override fun addRenderData(animatable: BloodKingEntity, relatedObject: Void?, renderState: R, partialTick: Float) {
        renderState.addGeckolibData(UUID_TICKET, animatable.uuid)
    }

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

        val state = renderPassInfo.renderState()
        val uuid = state.getGeckolibData(UUID_TICKET)!!

        LOCATORS.forEach {
            renderPassInfo.addLocatorPositionListener(it) { worldPos, modelPos, localPos ->
                GeoEntityLocatorService.addLocatorPos(uuid, it, worldPos)
            }
        }
    }
}