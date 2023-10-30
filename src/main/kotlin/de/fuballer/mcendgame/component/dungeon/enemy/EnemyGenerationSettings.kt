package de.fuballer.mcendgame.component.dungeon.enemy

import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.data.CustomEntityType
import de.fuballer.mcendgame.util.random.RandomOption
import de.fuballer.mcendgame.util.random.SortableRandomOption
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
        RandomOption(2, CustomEntityType.WITHER_SKELETON),
        RandomOption(4, CustomEntityType.CREEPER),
        RandomOption(3, CustomEntityType.WITCH),
        RandomOption(5, CustomEntityType.NECROMANCER),
        RandomOption(3, CustomEntityType.REAPER),
        RandomOption(5, CustomEntityType.STALKER),
        RandomOption(5, CustomEntityType.NAGA),
        RandomOption(5, CustomEntityType.CYCLOPS),
        RandomOption(5, CustomEntityType.HARPY),
        RandomOption(5, CustomEntityType.SUCCUBUS),
        RandomOption(5, CustomEntityType.INCUBUS),
    )

    val STRENGTH_EFFECTS = listOf(
        SortableRandomOption(250, 0, null),
        SortableRandomOption(80, 1, PotionEffect.STRENGTH_1),
        SortableRandomOption(35, 2, PotionEffect.STRENGTH_2),
        SortableRandomOption(15, 3, PotionEffect.STRENGTH_3),
        SortableRandomOption(3, 4, PotionEffect.STRENGTH_4),
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
}
