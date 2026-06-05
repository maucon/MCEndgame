package de.fuballer.mcendgame.client.component.entity.custom.entities.training_dummy

import com.geckolib.constant.dataticket.DataTicket
import net.minecraft.client.renderer.entity.state.HumanoidRenderState

class TrainingDummyRenderState : HumanoidRenderState() {
    var lastDamage: Float = 0F
    var highestDamage: Float = 0F
    var damageSum: Float = 0F
    var damagePerSecond: Float = 0F
    var damageDuration: Float = 0F
    var damageActive: Boolean = false

    override fun getDataMap(): Map<DataTicket<*>, Any> = mapOf()
}