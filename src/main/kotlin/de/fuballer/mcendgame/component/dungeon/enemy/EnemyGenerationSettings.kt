package de.fuballer.mcendgame.component.dungeon.enemy

import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.CustomEntityType
import de.fuballer.mcendgame.component.dungeon.enemy.data.DifficultyBaseStats
import de.fuballer.mcendgame.component.dungeon.enemy.data.MobDamagePrefix
import de.fuballer.mcendgame.component.dungeon.enemy.data.PotionEffect
import de.fuballer.mcendgame.util.random.RandomOption
import de.fuballer.mcendgame.util.random.SortableRandomOption
import org.bukkit.entity.EntityType
import java.util.*

object EnemyGenerationSettings {
    private const val MIN_MOBS_PER_TILE = 2
    private const val MAX_MOBS_PER_TILE = 5
    const val MOB_XZ_SPREAD = 1.5

    fun calculateMobCount(random: Random): Int {
        val possibleAddedMobs = MAX_MOBS_PER_TILE - MIN_MOBS_PER_TILE
        return MIN_MOBS_PER_TILE + random.nextInt(possibleAddedMobs)
    }

    val DUNGEON_MOBS = listOf(
        RandomOption(25, CustomEntityType.ZOMBIE),
        RandomOption(10, CustomEntityType.HUSK),
        RandomOption(15, CustomEntityType.SKELETON),
        RandomOption(5, CustomEntityType.STRAY),
        RandomOption(1, CustomEntityType.WITHER_SKELETON),
        RandomOption(4, CustomEntityType.CREEPER),
        RandomOption(3, CustomEntityType.WITCH),
        RandomOption(25, CustomEntityType.NECROMANCER)
    )

    val RESISTANCE_EFFECTS = listOf(
        SortableRandomOption(500, 0, null),
        SortableRandomOption(80, 1, PotionEffect.RESISTANCE_1),
        SortableRandomOption(25, 2, PotionEffect.RESISTANCE_2),
        SortableRandomOption(6, 3, PotionEffect.RESISTANCE_3),
        SortableRandomOption(1, 4, PotionEffect.RESISTANCE_4),
    )

    val SPEED_EFFECTS = listOf(
        SortableRandomOption(500, 0, null),
        SortableRandomOption(80, 1, PotionEffect.SPEED_1),
        SortableRandomOption(25, 2, PotionEffect.SPEED_2),
        SortableRandomOption(6, 3, PotionEffect.SPEED_3),
        SortableRandomOption(1, 4, PotionEffect.SPEED_4),
    )

    val FIRE_RESISTANCE_EFFECT = listOf(
        SortableRandomOption(100, 0, null),
        SortableRandomOption(6, 1, PotionEffect.FIRE_RESISTANCE),
    )

    val INVISIBILITY_EFFECT = listOf(
        RandomOption(100, null),
        RandomOption(10, PotionEffect.INVISIBILITY),
    )

    private const val EXTRA_MOB_STRENGTH_CHANCE = .4

    fun calculateStrengthAmplifier(random: Random, mapTier: Int): Int {
        var strengthAmplifier = -1
        for (i in 0 until mapTier)
            if (random.nextDouble() < EXTRA_MOB_STRENGTH_CHANCE) strengthAmplifier++

        return strengthAmplifier
    }

    val MELEE_MOBS = mapOf(
        EntityType.ZOMBIE to DifficultyBaseStats(2.5, 3.0, 4.5),
        EntityType.HUSK to DifficultyBaseStats(2.0, 3.0, 4.0),
        EntityType.WITHER_SKELETON to DifficultyBaseStats(5.0, 8.0, 12.0),
        EntityType.SPIDER to DifficultyBaseStats(2.0, 2.0, 3.0),
        EntityType.CAVE_SPIDER to DifficultyBaseStats(2.0, 2.0, 3.0),
        EntityType.SKELETON to DifficultyBaseStats(2.0, 2.0, 3.0),
        EntityType.STRAY to DifficultyBaseStats(2.0, 2.0, 3.0),
    )
    val RANGED_MOBS = mapOf(
        EntityType.SKELETON to DifficultyBaseStats(4.0, 4.0, 4.0),
        EntityType.STRAY to DifficultyBaseStats(3.0, 3.5, 4.5)
    )

    fun calculateMobPrefix(damage: Double) =
        MobDamagePrefix.values()
            .find { damage >= it.minDamage }
            ?.prefix
}
