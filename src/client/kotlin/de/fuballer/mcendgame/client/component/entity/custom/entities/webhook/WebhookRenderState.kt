package de.fuballer.mcendgame.client.component.entity.custom.entities.webhook

import net.minecraft.client.renderer.entity.state.EntityRenderState
import software.bernie.geckolib.constant.dataticket.DataTicket

class WebhookRenderState : EntityRenderState() {
    override fun getDataMap(): Map<DataTicket<*>, Any> = mapOf()
}