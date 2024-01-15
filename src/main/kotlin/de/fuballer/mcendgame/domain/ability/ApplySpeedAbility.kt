package de.fuballer.mcendgame.domain.ability

import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

val SPEED_EFFECT = PotionEffect(PotionEffectType.SPEED, 20, 4, false, false)

object ApplySpeedAbility : Ability {
    override fun cast(caster: LivingEntity, target: LivingEntity) {
        caster.addPotionEffect(SPEED_EFFECT)
    }
}