package de.fuballer.mcendgame.client.component.entity.custom.entities.webshot

import net.minecraft.client.renderer.entity.state.ArrowRenderState
import software.bernie.geckolib.constant.dataticket.DataTicket

class WebshotRenderState : ArrowRenderState() {
    override fun getDataMap(): Map<DataTicket<*>, Any> = mapOf()
}