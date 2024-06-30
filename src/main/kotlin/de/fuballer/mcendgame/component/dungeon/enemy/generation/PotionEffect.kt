package de.fuballer.mcendgame.component.dungeon.enemy.generation

import org.bukkit.potion.PotionEffectType

enum class PotionEffect(
    private val type: PotionEffectType,
    private val amplifier: Int,
    private val duration: Int = Int.MAX_VALUE
) {
    STRENGTH_1(PotionEffectType.STRENGTH, 0),
    STRENGTH_2(PotionEffectType.STRENGTH, 1),
    STRENGTH_3(PotionEffectType.STRENGTH, 2),
    STRENGTH_4(PotionEffectType.STRENGTH, 3),
    RESISTANCE_1(PotionEffectType.RESISTANCE, 0),
    RESISTANCE_2(PotionEffectType.RESISTANCE, 1),
    RESISTANCE_3(PotionEffectType.RESISTANCE, 2),
    RESISTANCE_4(PotionEffectType.RESISTANCE, 3),
    SPEED_1(PotionEffectType.SPEED, 0),
    SPEED_2(PotionEffectType.SPEED, 1),
    SPEED_3(PotionEffectType.SPEED, 2),
    SPEED_4(PotionEffectType.SPEED, 3),
    FIRE_RESISTANCE(PotionEffectType.FIRE_RESISTANCE, 0),
    WIND_CHARGED(PotionEffectType.WIND_CHARGED, 0),
    WEAVING(PotionEffectType.WEAVING, 0),
    INVISIBILITY(PotionEffectType.INVISIBILITY, 0),
    ;

    fun getPotionEffect() = org.bukkit.potion.PotionEffect(type, duration, amplifier, false, false)
}
