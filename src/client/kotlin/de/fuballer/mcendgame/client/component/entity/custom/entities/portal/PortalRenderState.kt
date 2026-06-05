package de.fuballer.mcendgame.client.component.entity.custom.entities.portal

import com.geckolib.constant.dataticket.DataTicket
import de.fuballer.mcendgame.client.component.entity.custom.entities.portal.type.PortalRenderType
import de.fuballer.mcendgame.client.component.entity.custom.entities.portal.type.default_.DefaultPortalRenderType
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import net.minecraft.world.entity.AnimationState

class PortalRenderState : LivingEntityRenderState() {
    var type: PortalRenderType = DefaultPortalRenderType()

    val openAnimationState = AnimationState()
    val idleAnimationState = AnimationState()
    val closeAnimationState = AnimationState()

    override fun getDataMap(): Map<DataTicket<*>, Any> = mapOf()
}