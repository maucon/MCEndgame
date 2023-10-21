package de.fuballer.mcendgame.component.dungeon.enemy.custom_entity

import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.data.CustomEntityData

@Suppress("BooleanLiteralArgument")
object CustomEntitySettings {
    val ZOMBIE_DATA = CustomEntityData(
        true, false, true, true, false,
        20.0, 0.0, 5.0, 2.5, 0.23, 0.0
    )
    val HUSK_DATA = CustomEntityData(
        true, false, true, true, false,
        25.0, 0.0, 5.0, 2.5, 0.23, 0.0
    )
    val SKELETON_DATA = CustomEntityData(
        true, true, true, true, false,
        15.0, 0.0, 5.0, 2.5, 0.25, 0.0
    )
    val STRAY_DATA = CustomEntityData(
        true, true, true, true, false,
        15.0, 0.0, 5.0, 2.5, 0.25, 0.0
    )
    val WITHER_SKELETON_DATA = CustomEntityData(
        true, false, true, true, false,
        20.0, 0.0, 8.0, 3.0, 0.25, 0.0
    )
    val CREEPER_DATA = CustomEntityData(
        true, false, true, true, true,
        5.0, 0.0, 0.0, 0.0, 0.3, 0.01
    )
    val WITCH_DATA = CustomEntityData(
        true, false, true, true, true,
        15.0, 0.0, 0.0, 0.0, 0.25, 0.0
    )

    val RAVAGER_DATA = CustomEntityData(
        true, false, true, false, true,
        1.0, 0.0, 0.0, 0.0, 0.0, 0.0
    )

    val NECROMANCER_DATA = CustomEntityData(
        true, false, true, false, true,
        15.0, 0.0, 0.0, 0.0, 0.4, 0.0
    )
    val REAPER_DATA = CustomEntityData(
        true, false, true, false, true,
        20.0, 0.0, 10.0, 3.5, 0.3, 0.0
    )

    val DEMONIC_GOLEM_DATA = CustomEntityData(
        true, false, true, false, true,
        1.0, 0.0, 0.0, 0.0, 0.0, 0.0
    )
    val MINOTAUR_DATA = CustomEntityData(
        true, false, true, false, true,
        1.0, 0.0, 0.0, 0.0, 0.0, 0.0
    )
    val NAGA_DATA = CustomEntityData(
        true, true, true, false, true,
        1.0, 0.0, 0.0, 0.0, 0.0, 0.0
    )

    val STONE_PILLAR_DATA = CustomEntityData(
        false, false, false, false, true,
        1.0, 0.0, 0.0, 0.0, 0.0, 0.0
    )

    val POISON_SPIT_DATA = CustomEntityData(
        false, false, false, false, true,
        1.0, 0.0, 0.0, 0.0, 0.0, 0.0
    )
}