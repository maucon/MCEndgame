package de.fuballer.mcendgame.component.custom_entity.ability.abilities

import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

val SPEED_EFFECT = PotionEffect(PotionEffectType.SPEED, 20, 4, false, false)

object ApplySpeedAbility : Ability {
    override fun cast(caster: LivingEntity) {
        caster.addPotionEffect(SPEED_EFFECT)
    }
}