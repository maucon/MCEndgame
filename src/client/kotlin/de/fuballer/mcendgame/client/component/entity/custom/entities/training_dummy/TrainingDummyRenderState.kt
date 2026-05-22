package de.fuballer.mcendgame.client.component.entity.custom.entities.training_dummy

import net.minecraft.client.render.entity.state.BipedEntityRenderState
import software.bernie.geckolib.constant.dataticket.DataTicket

class TrainingDummyRenderState : BipedEntityRenderState() {
    override fun getDataMap(): Map<DataTicket<*>, Any> = mapOf()
}