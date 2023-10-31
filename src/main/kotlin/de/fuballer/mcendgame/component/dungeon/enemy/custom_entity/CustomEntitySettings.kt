package de.fuballer.mcendgame.component.dungeon.enemy.custom_entity

import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.data.CustomEntityData

@Suppress("BooleanLiteralArgument")
object CustomEntitySettings {
    val ZOMBIE_DATA = CustomEntityData(
        true, false, true, true, false,false,
        20.0, 0.0, 5.0, 2.5, 0.23, 0.0
    )
    val HUSK_DATA = CustomEntityData(
        true, false, true, true, false,false,
        25.0, 0.0, 5.0, 2.5, 0.23, 0.0
    )
    val SKELETON_DATA = CustomEntityData(
        true, true, true, true, false,false,
        15.0, 0.0, 5.0, 2.5, 0.25, 0.0
    )
    val STRAY_DATA = CustomEntityData(
        true, true, true, true, false,false,
        15.0, 0.0, 5.0, 2.5, 0.25, 0.0
    )
    val WITHER_SKELETON_DATA = CustomEntityData(
        true, false, true, true, false,false,
        20.0, 0.0, 8.0, 3.0, 0.25, 0.0
    )
    val CREEPER_DATA = CustomEntityData(
        true, false, true, true, true,false,
        5.0, 0.0, 0.0, 0.0, 0.3, 0.01
    )
    val WITCH_DATA = CustomEntityData(
        true, false, true, true, true,false,
        15.0, 0.0, 0.0, 0.0, 0.25, 0.0
    )

    val NECROMANCER_DATA = CustomEntityData(
        true, false, true, false, true, true,
        15.0, 0.0, 0.0, 0.0, 0.4, 0.0
    )
    val REAPER_DATA = CustomEntityData(
        true, false, true, false, true, true,
        20.0, 0.0, 10.0, 3.5, 0.3, 0.0
    )
    val CHUPACABRA_DATA = CustomEntityData(
        true, false, true, false, true, true,
        5.0, 0.0, 5.0, 2.5, 0.35, 0.0
    )
    val STALKER_DATA = CustomEntityData(
        true, false, true, false, true, true,
        10.0, 0.0, 4.0, 1.5, 0.45, 0.0
    )

    val DEMONIC_GOLEM_DATA = CustomEntityData(
        true, false, true, false, true, true,
        1.0, 0.0, 0.0, 0.0, 0.0, 0.0
    )
    val MINOTAUR_DATA = CustomEntityData(
        true, false, true, false, true, true,
        1.0, 0.0, 0.0, 0.0, 0.0, 0.0
    )
    val NAGA_DATA = CustomEntityData(
        true, true, true, false, true, true,
        10.0, 0.0, 5.0, 2.5, 0.35, 0.0
    )
    val CYCLOPS_DATA = CustomEntityData(
        true, false, true, false, true, true,
        20.0, 0.0, 5.0, 2.5, 0.25, 0.0
    )
    val HARPY_DATA = CustomEntityData(
        true, true, true, false, true, true,
        14.0, 0.0, 7.0, 3.0, 0.33, 0.0
    )
    val CERBERUS_DATA = CustomEntityData(
        true, false, true, false, true, true,
        1.0, 0.0, 0.0, 0.0, 0.0, 0.0
    )
    val SUCCUBUS_DATA = CustomEntityData(
        true, false, true, false, true, true,
        15.0, 0.0, 7.0, 3.0, 0.3, 0.0
    )
    val INCUBUS_DATA = CustomEntityData(
        true, false, true, false, true, true,
        22.0, 0.0, 3.0, 2.0, 0.25, 0.0
    )

    val STONE_PILLAR_DATA = CustomEntityData(
        false, false, false, false, true, true,
        1.0, 0.0, 0.0, 0.0, 0.0, 0.0
    )

    val POISON_SPIT_DATA = CustomEntityData(
        false, false, false, false, true, false,
        1.0, 0.0, 0.0, 0.0, 0.0, 0.0
    )
}