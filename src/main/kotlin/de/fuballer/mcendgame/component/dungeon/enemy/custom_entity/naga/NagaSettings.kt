package de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.naga

import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object NagaSettings {
    const val EXTRA_PROJECTILES = 2 //multiple of 2
    val POISON_EFFECT = PotionEffect(PotionEffectType.POISON, 60, 2, false, false)
}