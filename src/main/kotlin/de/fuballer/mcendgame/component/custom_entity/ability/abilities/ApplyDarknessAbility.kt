package de.fuballer.mcendgame.component.custom_entity.ability.abilities

import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.math.pow
import kotlin.math.sqrt

private const val DARKNESS_EFFECT_RADIUS = 30
private val DARKNESS_EFFECT = PotionEffect(PotionEffectType.DARKNESS, 160, 1, true)

object ApplyDarknessAbility : Ability {
    override fun cast(caster: LivingEntity) {
        val casterLocation = caster.location

        for (player in caster.world.players) {
            val playerLocation = player.location
            if (sqrt((casterLocation.x - playerLocation.x).pow(2) + (casterLocation.z - playerLocation.z).pow(2)) < DARKNESS_EFFECT_RADIUS) {
                player.addPotionEffect(DARKNESS_EFFECT)
            }
        }
    }
}