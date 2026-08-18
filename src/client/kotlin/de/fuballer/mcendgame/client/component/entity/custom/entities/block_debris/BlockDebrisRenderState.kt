package de.fuballer.mcendgame.client.component.entity.custom.entities.block_debris

import com.geckolib.constant.dataticket.DataTicket
import net.minecraft.client.renderer.block.MovingBlockRenderState
import net.minecraft.client.renderer.entity.state.EntityRenderState

class BlockDebrisRenderState : EntityRenderState() {
    val movingBlockRenderState: MovingBlockRenderState = MovingBlockRenderState()

    override fun getDataMap(): Map<DataTicket<*>, Any> = mapOf()
}
