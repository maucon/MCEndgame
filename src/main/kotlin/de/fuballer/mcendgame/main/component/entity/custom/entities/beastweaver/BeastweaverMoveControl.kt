package de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver

import net.minecraft.world.entity.ai.control.MoveControl

class BeastweaverMoveControl(
    private val beastweaver: BeastweaverEntity,
) : MoveControl(beastweaver) {
    override fun rotlerp(current: Float, target: Float, maxChange: Float): Float {
        if (beastweaver.isRhinoCharging()) return super.rotlerp(current, target, beastweaver.getRhinoChargeMaxYawChange())
        return super.rotlerp(current, target, maxChange)
    }
}