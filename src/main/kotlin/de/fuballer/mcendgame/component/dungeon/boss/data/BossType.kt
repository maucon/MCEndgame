package de.fuballer.mcendgame.component.dungeon.boss.data

import de.fuballer.mcendgame.component.custom_entity.data.CustomEntityType
import de.fuballer.mcendgame.util.random.RandomOption

enum class BossType(
    val customEntityType: CustomEntityType,
    val abilities: List<RandomOption<BossAbility>>,
    val baseHealth: Double,
    val healthPerTier: Double,
    val baseDamage: Double,
    val damagePerTier: Double,
    val speed: Double,
) {
    DEMONIC_GOLEM(
        CustomEntityType.DEMONIC_GOLEM,
        listOf(
            RandomOption(30, BossAbility.DARKNESS),
            RandomOption(50, BossAbility.FIRE_CASCADE),
            RandomOption(40, BossAbility.GRAVITATION_PILLAR),
        ),
        150.0, 8.0,
        20.0, 3.0,
        0.2
    ),
    MINOTAUR(
        CustomEntityType.MINOTAUR,
        listOf(
            RandomOption(20, BossAbility.SPEED),
            RandomOption(50, BossAbility.LEAP),
        ),
        100.0, 5.0,
        12.0, 3.0,
        0.4
    ),
    CERBERUS(
        CustomEntityType.CERBERUS,
        listOf(
            RandomOption(30, BossAbility.FIRE_ARROWS),
            RandomOption(50, BossAbility.FIRE_CASCADE),
            RandomOption(25, BossAbility.DARKNESS),
        ),
        100.0, 5.0,
        15.0, 3.5,
        0.35
    ),
    MANDRAGORA(
        CustomEntityType.MANDRAGORA,
        listOf(
            RandomOption(30, BossAbility.POISON_CLOUD),
            RandomOption(30, BossAbility.VINES),
        ),
        120.0, 6.0,
        12.0, 3.0,
        0.3
    );
}
