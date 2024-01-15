package de.fuballer.mcendgame.domain.ability

import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.math.pow
import kotlin.math.sqrt

const val DARKNESS_EFFECT_RADIUS = 30
val DARKNESS_EFFECT = PotionEffect(PotionEffectType.DARKNESS, 160, 1, true)

object ApplyDarknessAbility : Ability {
    override fun cast(caster: LivingEntity, target: LivingEntity) {
        val bLoc = caster.location
        for (player in caster.world.players) {
            val pLoc = player.location
            if (sqrt((bLoc.x - pLoc.x).pow(2) + (bLoc.z - pLoc.z).pow(2)) < DARKNESS_EFFECT_RADIUS) {
                player.addPotionEffect(DARKNESS_EFFECT)
            }
        }
    }
}