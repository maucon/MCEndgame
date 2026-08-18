package de.fuballer.mcendgame.main.component.entity.custom.entities.beastweaver

import de.fuballer.mcendgame.main.component.entity.custom.goals.DisableAbleGoal
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class BeastweaverRhinoChargeControlGoal(
    private val beastweaver: BeastweaverEntity,
) : DisableAbleGoal() {
    init {
        setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK))
    }

    override fun canUse(): Boolean {
        if (!super.canUse()) return false
        return beastweaver.isRhinoCharging()
    }

    override fun canContinueToUse() = canUse()

    override fun start() {
        beastweaver.navigation.stop()
    }

    override fun tick() {
        val target = beastweaver.target

        if (target != null) {
            beastweaver.lookControl.setLookAt(target, 30.0f, 30.0f)

            val targetPos = target.position()
            beastweaver.moveControl.setWantedPosition(
                targetPos.x,
                targetPos.y,
                targetPos.z,
                1.0,
            )
        } else {
            val yaw = Math.toRadians(beastweaver.yBodyRot.toDouble())
            val forwardX = -sin(yaw)
            val forwardZ = cos(yaw)
            val distance = 4.0

            beastweaver.moveControl.setWantedPosition(
                beastweaver.x + forwardX * distance,
                beastweaver.y,
                beastweaver.z + forwardZ * distance,
                1.0,
            )
        }
    }

    override fun requiresUpdateEveryTick() = true
}