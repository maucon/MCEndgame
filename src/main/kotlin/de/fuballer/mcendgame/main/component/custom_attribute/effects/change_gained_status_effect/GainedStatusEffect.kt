package de.fuballer.mcendgame.main.component.custom_attribute.effects.change_gained_status_effect

import net.minecraft.core.Holder
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffects

enum class GainedStatusEffect(
    val displayName: String,
    val effect: Holder<MobEffect>,
) {
    REGENERATION("Regeneration", MobEffects.REGENERATION),
    POISON("Poison", MobEffects.POISON),
    STRENGTH("Strength", MobEffects.STRENGTH),
    WEAKNESS("Weakness", MobEffects.WEAKNESS),
    SPEED("Speed", MobEffects.SPEED),
    SLOWNESS("Slowness", MobEffects.SLOWNESS),
    WITHER("Wither", MobEffects.WITHER),
    RESISTANCE("Resistance", MobEffects.RESISTANCE);

    companion object {
        fun fromString(name: String) = entries.firstOrNull { it.displayName == name }
    }
}