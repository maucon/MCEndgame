package de.fuballer.mcendgame.client.component.block.totem_statue

import com.geckolib.constant.dataticket.DataTicket
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState

class TotemStatueBlockEntityRenderState : BlockEntityRenderState() {
    var rotation: Int = 0
    var activeTicks: Int = -1
    
    override fun getDataMap(): Map<DataTicket<*>, Any> = mapOf()
}