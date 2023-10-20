package de.fuballer.mcendgame.component.dungeon.boss.data

import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.CustomEntityType
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
    RAVAGER(
        CustomEntityType.RAVAGER,
        listOf(
            RandomOption(40, BossAbility.ARROWS),
            RandomOption(30, BossAbility.FIRE_ARROWS),
            RandomOption(50, BossAbility.SPEED),
        ),
        100.0, 5.0,
        12.0, 2.0,
        0.35
    ),
    DEMONIC_GOLEM(
        CustomEntityType.DEMONIC_GOLEM,
        listOf(
            RandomOption(30, BossAbility.DARKNESS),
            RandomOption(50, BossAbility.FIRE_CASCADE),
            RandomOption(40, BossAbility.GRAVITATION_PILLAR),
        ),
        150.0, 10.0,
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
    NAGA(
        CustomEntityType.NAGA,
        listOf(
            RandomOption(30, BossAbility.SPEED),
            RandomOption(50, BossAbility.POISON_CLOUD),
        ),
        80.0, 4.0,
        15.0, 4.0,
        0.3
    );

    companion object {
        fun getRandom() = entries.toTypedArray().random()
    }
}
