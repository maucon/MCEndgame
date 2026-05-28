package de.fuballer.mcendgame.main.component.custom_attribute.effects.burn_enemy_on_walk

object BurnEnemyOnWalkSettings {
    const val BURN_DURATION = 4F // seconds

    fun getSparkTravelTime(distance: Float) = (distance * 1.5).toInt()
}