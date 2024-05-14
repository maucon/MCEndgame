package de.fuballer.mcendgame.component.dungeon.boss

import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object DungeonBossSettings {
    private const val CORRUPTION_CHANCE_PER_TIER = 0.2

    fun calculateCorruptDropChance(mapTier: Int) = CORRUPTION_CHANCE_PER_TIER * mapTier

    private const val DOUBLE_CORRUPTION_TIER_OFFSET = 5.0
    private const val DOUBLE_CORRUPTION_CHANCE_PER_TIER = 0.15

    fun calculateDoubleCorruptDropChance(mapTier: Int): Double {
        if (mapTier <= DOUBLE_CORRUPTION_TIER_OFFSET) return 0.0
        return DOUBLE_CORRUPTION_CHANCE_PER_TIER * (mapTier - DOUBLE_CORRUPTION_TIER_OFFSET)
    }

    val BOSS_POTION_EFFECTS = listOf(
        PotionEffect(PotionEffectType.FIRE_RESISTANCE, Int.MAX_VALUE, 0, false, false)
    )

    const val EMPOWERED_LOOT_MULTIPLIER = 1.2
}
