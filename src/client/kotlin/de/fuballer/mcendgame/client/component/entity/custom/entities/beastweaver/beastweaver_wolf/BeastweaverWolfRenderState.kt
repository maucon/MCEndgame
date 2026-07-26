package de.fuballer.mcendgame.client.component.entity.custom.entities.beastweaver.beastweaver_wolf

import com.geckolib.constant.dataticket.DataTicket
import de.fuballer.mcendgame.client.component.entity.custom.entities.beastweaver.BeastweaverGradientData
import net.minecraft.client.renderer.entity.state.WolfRenderState

class BeastweaverWolfRenderState : WolfRenderState() {
    var gradientData: BeastweaverGradientData? = null

    override fun getDataMap(): Map<DataTicket<*>, Any> = mapOf()
}