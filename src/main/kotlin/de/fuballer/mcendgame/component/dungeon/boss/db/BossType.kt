package de.fuballer.mcendgame.component.dungeon.boss.db

import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.CustomEntityType
import de.fuballer.mcendgame.random.RandomOption

enum class BossType(
    val customEntityType: CustomEntityType,
    val abilities: List<RandomOption<BossAbility>>
) {
    RAVAGER(
        CustomEntityType.RAVAGER,
        listOf(
            RandomOption(40, BossAbility.ARROWS),
            RandomOption(30, BossAbility.FIRE_ARROWS),
            RandomOption(50, BossAbility.SPEED),
        )
    ),
    DEMONIC_GOLEM(
        CustomEntityType.DEMONIC_GOLEM,
        listOf(
            RandomOption(50, BossAbility.LEAP),
            RandomOption(30, BossAbility.DARKNESS),
            RandomOption(50, BossAbility.FIRE_CASCADE),
        )
    );

    companion object {
        fun getRandom() = values().random()
    }
}