package de.fuballer.mcendgame.main.component.custom_attribute.effects.projectile

object AdditionalProjectilesSettings {
    const val SPREAD_PER_PROJECTILE = 5F // the resulting cone has an angle of this times two
    val SPREAD_PRE_PROJECTILE_RAD = Math.toRadians(SPREAD_PER_PROJECTILE.toDouble()).toFloat()
}