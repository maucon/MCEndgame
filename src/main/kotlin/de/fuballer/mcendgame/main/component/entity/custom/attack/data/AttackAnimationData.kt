package de.fuballer.mcendgame.main.component.entity.custom.attack.data

import com.geckolib.animatable.GeoEntity
import de.fuballer.mcendgame.main.component.entity.custom.attack.AttackPose

data class AttackAnimationData(
    val startPose: AttackPose,
    val endPose: AttackPose,
    val animControllerId: String,
    val animId: String,
) {
    fun triggerAnimation(animateAble: GeoEntity) = animateAble.triggerAnim(animControllerId, animId)
}