package de.fuballer.mcendgame.main.component.entity.custom.attack.data

import com.geckolib.animatable.GeoEntity
import de.fuballer.mcendgame.main.component.entity.custom.attack.AttackPose
import de.fuballer.mcendgame.main.component.entity.custom.interfaces.CustomAttacksMob

data class AttackAnimationData(
    val startPose: AttackPose,
    val endPose: AttackPose,
    private val animControllerId: String,
    private val animId: String,
) {
    fun triggerAnimation(
        animateAble: GeoEntity,
        attackSpeed: Double,
    ) {
        if (attackSpeed > 0 && animateAble is CustomAttacksMob<*>)
            animateAble.setAttackAnimationSpeed(attackSpeed.toFloat())

        animateAble.triggerAnim(animControllerId, animId)
    }
}