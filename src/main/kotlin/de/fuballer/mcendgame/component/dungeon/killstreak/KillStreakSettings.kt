package de.fuballer.mcendgame.component.dungeon.killstreak

import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle

object KillStreakSettings {
    const val GEAR_DROP_CHANCE_MULTIPLIER_PER_STREAK = 1.0 / 150

    const val MAX_TIMER = 8 * 20L
    const val TIME_PER_HIT = 20
    const val MIN_ATTACK_COOLDOWN_FOR_EXTRA_TIME = 0.8
    const val TIMER_PERIOD = 2L // in ticks

    val BAR_COLOR = BarColor.RED
    val BAR_STYLE = BarStyle.SOLID
}
