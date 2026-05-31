package de.fuballer.mcendgame.client.component.entity.custom.entities.training_dummy

import net.minecraft.client.render.entity.state.BipedEntityRenderState
import software.bernie.geckolib.constant.dataticket.DataTicket

class TrainingDummyRenderState : BipedEntityRenderState() {
    var lastDamage: Float = 0F
    var highestDamage: Float = 0F
    var damageSum: Float = 0F
    var damagePerSecond: Float = 0F
    var damageDuration: Float = 0F
    var damageActive: Boolean = false

    override fun getDataMap(): Map<DataTicket<*>, Any> = mapOf()
}