package de.fuballer.mcendgame.client.component.entity.custom.entities.webshot

import com.geckolib.constant.dataticket.DataTicket
import net.minecraft.client.renderer.entity.state.ArrowRenderState

class WebshotRenderState : ArrowRenderState() {
    override fun getDataMap(): Map<DataTicket<*>, Any> = mapOf()
}