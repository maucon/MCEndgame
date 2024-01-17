package de.fuballer.mcendgame.component.dungeon.killstreak

import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle

object KillStreakSettings {
    const val GEAR_DROP_CHANCE_MULTIPLIER_PER_STREAK = 1.0 / 150

    const val TIMER_MS = 8000
    const val TIME_PER_HIT = 500
    const val MIN_DMG_FOR_EXTRA_TIME = 4
    const val TIMER_PERIOD = 100L

    val BAR_COLOR = BarColor.RED
    val BAR_STYLE = BarStyle.SOLID
}
