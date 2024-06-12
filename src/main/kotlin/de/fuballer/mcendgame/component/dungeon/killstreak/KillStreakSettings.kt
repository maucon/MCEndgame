package de.fuballer.mcendgame.component.dungeon.killstreak

import de.fuballer.mcendgame.component.dungeon.modifier.Modifier
import de.fuballer.mcendgame.component.dungeon.modifier.ModifierOperation
import de.fuballer.mcendgame.component.dungeon.modifier.ModifierType
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle

object KillStreakSettings {
    const val MAX_TIMER = 12 * 20L
    const val TIME_PER_HIT = 25
    const val MIN_ATTACK_COOLDOWN_FOR_EXTRA_TIME = 0.8
    const val TIMER_PERIOD = 2L // in ticks

    val BAR_COLOR = BarColor.RED
    val BAR_STYLE = BarStyle.SOLID

    private const val KILLSTREAK_MODIFIER_VALUE_PER_STREAK = 1.0 / 150
    const val KILLSTREAK_MODIFIER_SOURCE = "killstreak"

    fun createKillStreakModifier(streak: Int) =
        Modifier(ModifierType.MAGIC_FIND, ModifierOperation.INCREASE, KILLSTREAK_MODIFIER_VALUE_PER_STREAK * streak, KILLSTREAK_MODIFIER_SOURCE)
}