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

    const val BOSS_ABILITY_CHECK_PERIOD = 5 // in ticks

    const val ARROWS_COUNT = 5
    const val ARROWS_TIME_DIFFERENCE = 4L // in ticks

    val SPEED_EFFECT = PotionEffect(PotionEffectType.SPEED, 20, 4, false, false)

    const val FIRE_CASCADE_DISTANCE = 1
    const val FIRE_CASCADE_STEPS_AFTER_PLAYER = 15
    const val FIRE_CASCADE_ACTIVATION_DELAY = 10L // in ticks

    const val FIRE_CASCADE_STEP_DELAY = 0.7 //in Ticks

    const val FIRE_CASCADE_STEPS_PER_SOUND = 5
    const val FIRE_CASCADE_DAMAGE = 5.0
    const val FIRE_CASCADE_DAMAGE_PER_LEVEL = 3.0
    const val FIRE_CASCADE_FIRE_TICKS = 100

    const val DARKNESS_EFFECT_RADIUS = 30
    val DARKNESS_EFFECT = PotionEffect(PotionEffectType.DARKNESS, 160, 1, true)

    const val GRAVITATION_PILLAR_RANGE = 20.0
    const val GRAVITATION_PILLAR_COOLDOWN = 40L //in Ticks

    fun getBossAbilityCooldown(bossLevel: Int) = 30 + 120 / (1 + bossLevel / 5)
}
