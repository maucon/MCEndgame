package de.fuballer.mcendgame.component.dungeon.enemy.generation

import de.fuballer.mcendgame.util.random.RandomOption
import de.fuballer.mcendgame.util.random.SortableRandomOption
import kotlin.math.pow
import kotlin.random.Random

object EnemyGenerationSettings {
    val STRENGTH_EFFECTS = listOf(
        SortableRandomOption(2500, 0, null),
        SortableRandomOption(400, 1, PotionEffect.STRENGTH_1),
        SortableRandomOption(100, 2, PotionEffect.STRENGTH_2),
        SortableRandomOption(20, 3, PotionEffect.STRENGTH_3),
        SortableRandomOption(3, 4, PotionEffect.STRENGTH_4),
    )

    val RESISTANCE_EFFECTS = listOf(
        SortableRandomOption(5000, 0, null),
        SortableRandomOption(300, 1, PotionEffect.RESISTANCE_1),
        SortableRandomOption(75, 2, PotionEffect.RESISTANCE_2),
        SortableRandomOption(15, 3, PotionEffect.RESISTANCE_3),
        SortableRandomOption(1, 4, PotionEffect.RESISTANCE_4),
    )

    val SPEED_EFFECTS = listOf(
        SortableRandomOption(5000, 0, null),
        SortableRandomOption(300, 1, PotionEffect.SPEED_1),
        SortableRandomOption(75, 2, PotionEffect.SPEED_2),
        SortableRandomOption(15, 3, PotionEffect.SPEED_3),
        SortableRandomOption(1, 4, PotionEffect.SPEED_4),
    )

    val FIRE_RESISTANCE_EFFECT = listOf(
        SortableRandomOption(100, 0, null),
        SortableRandomOption(5, 1, PotionEffect.FIRE_RESISTANCE),
    )

    val ON_DEATH_EFFECTS = listOf(
        RandomOption(20, null),
        RandomOption(1, PotionEffect.WIND_CHARGED),
        RandomOption(1, PotionEffect.WEAVING),
    )

    val INVISIBILITY_EFFECT = listOf(
        RandomOption(10, null),
        RandomOption(1, PotionEffect.INVISIBILITY),
    )

    fun getRandomScale(random: Random) = 1.0 + 0.2 * random.nextDouble().pow(3) * if (random.nextBoolean()) 1 else -1
}