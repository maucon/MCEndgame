package de.fuballer.mcendgame.client.component.entity.custom.entities.webhook

import com.geckolib.constant.dataticket.DataTicket
import net.minecraft.client.renderer.entity.state.EntityRenderState

class WebhookRenderState : EntityRenderState() {
    override fun getDataMap(): Map<DataTicket<*>, Any> = mapOf()
}