package de.fuballer.mcendgame.client.component.entity.custom.entities.swamp_golem

import com.geckolib.constant.dataticket.DataTicket
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import net.minecraft.world.entity.AnimationState

class SwampGolemRenderState : LivingEntityRenderState() {
    val slamAnimationState: AnimationState = AnimationState()
    val idleAnimationState: AnimationState = AnimationState()
    val walkAnimationState: AnimationState = AnimationState()

    override fun getDataMap(): Map<DataTicket<*>, Any> = mapOf()
}