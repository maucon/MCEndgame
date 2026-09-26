package de.fuballer.mcendgame.main.component.custom_attribute.effects

import kotlin.math.min

object BowPullUtil {
    fun getPullProgress(useTicks: Int, fullPullTicks: Int): Float {
        val percentage = useTicks / fullPullTicks.toFloat()
        val progress = (percentage * percentage + percentage * 2.0F) / 3.0F
        return min(progress, 1F)
    }
}