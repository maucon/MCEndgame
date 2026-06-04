package de.fuballer.mcendgame.client.component.entity.custom.entities.arachne

import de.fuballer.mcendgame.client.component.entity.custom.data.MultipleEntityConnectionData
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import net.minecraft.world.entity.AnimationState
import software.bernie.geckolib.constant.dataticket.DataTicket

class ArachneRenderState : LivingEntityRenderState() {
    val idleAnimationState: AnimationState = AnimationState()
    val walkAnimationState: AnimationState = AnimationState()
    val walkBWAnimationState: AnimationState = AnimationState()

    val spitAnimationState: AnimationState = AnimationState()
    val meleeAttackAnimationState: AnimationState = AnimationState()

    var isSaddled: Boolean = false
    var moveSpeed: Float = 0F

    var webHookData: MultipleEntityConnectionData? = null

    override fun getDataMap(): Map<DataTicket<*>, Any> = mapOf()
}