package de.fuballer.mcendgame.main.component.entity.custom.attack.data.status_effect

import net.minecraft.core.Holder
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity

data class StatusEffectData(
    private val type: Holder<MobEffect>,
    private val amplifier: Int = 0,
    private val duration: Int = Int.MAX_VALUE,
    private val ambient: Boolean = false,
    private val particles: Boolean = true,
) {
    fun apply(
        entity: LivingEntity,
    ) {
        val instance = MobEffectInstance(type, duration, amplifier, ambient, particles)
        entity.addEffect(instance)
    }
}