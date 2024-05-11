package de.fuballer.mcendgame.component.dungeon.enemy.generation

import de.fuballer.mcendgame.util.random.RandomOption
import de.fuballer.mcendgame.util.random.SortableRandomOption
import org.bukkit.potion.PotionEffectType

object EnemyGenerationSettings {
    const val SPECIAL_MOB_COUNT = 3
    val INIT_POTION_EFFECT_TYPE: PotionEffectType = PotionEffectType.LUCK
    val INIT_POTION_EFFECT = org.bukkit.potion.PotionEffect(INIT_POTION_EFFECT_TYPE, 1, 0, false, false)

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