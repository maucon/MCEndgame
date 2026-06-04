package de.fuballer.mcendgame.main.component.dungeon.enemy.potion_effect

import net.minecraft.core.Holder
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects

enum class PotionEffect(
    private val type: Holder<MobEffect>,
    private val amplifier: Int = 0,
    private val duration: Int = Int.MAX_VALUE
) {
    STRENGTH_1(MobEffects.STRENGTH, 0),
    STRENGTH_2(MobEffects.STRENGTH, 1),
    STRENGTH_3(MobEffects.STRENGTH, 2),
    STRENGTH_4(MobEffects.STRENGTH, 3),
    RESISTANCE_1(MobEffects.RESISTANCE, 0),
    RESISTANCE_2(MobEffects.RESISTANCE, 1),
    RESISTANCE_3(MobEffects.RESISTANCE, 2),
    RESISTANCE_4(MobEffects.RESISTANCE, 3),
    SPEED_1(MobEffects.SPEED, 0),
    SPEED_2(MobEffects.SPEED, 1),
    SPEED_3(MobEffects.SPEED, 2),
    SPEED_4(MobEffects.SPEED, 3),
    FIRE_RESISTANCE(MobEffects.FIRE_RESISTANCE),
    WIND_CHARGED(MobEffects.WIND_CHARGED),
    WEAVING(MobEffects.WEAVING),
    INVISIBILITY(MobEffects.INVISIBILITY),
    ;

    fun getEffectInstance(particles: Boolean = true) =
        MobEffectInstance(type, duration, amplifier, false, particles)
}