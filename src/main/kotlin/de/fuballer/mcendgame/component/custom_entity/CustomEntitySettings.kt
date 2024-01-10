package de.fuballer.mcendgame.component.custom_entity

import de.fuballer.mcendgame.component.custom_entity.data.CustomEntityData

object CustomEntitySettings {
    val ZOMBIE_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = false, isSilent = false,
        baseHealth = 20.0, healthPerTier = 0.0, baseDamage = 5.0, damagePerTier = 2.5, baseSpeed = 0.23, speedPerTier = 0.0
    )
    val HUSK_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = false, isSilent = false,
        baseHealth = 25.0, healthPerTier = 0.0, baseDamage = 5.0, damagePerTier = 2.5, baseSpeed = 0.23, speedPerTier = 0.0
    )
    val SKELETON_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = true, canHaveArmor = true, dropBaseLoot = false, hideEquipment = false, isSilent = false,
        baseHealth = 15.0, healthPerTier = 0.0, baseDamage = 5.0, damagePerTier = 2.5, baseSpeed = 0.25, speedPerTier = 0.0
    )
    val MELEE_SKELETON_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = false, isSilent = false,
        baseHealth = 20.0, healthPerTier = 0.0, baseDamage = 5.0, damagePerTier = 2.5, baseSpeed = 0.25, speedPerTier = 0.0
    )
    val STRAY_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = true, canHaveArmor = true, dropBaseLoot = false, hideEquipment = false, isSilent = false,
        baseHealth = 15.0, healthPerTier = 0.0, baseDamage = 5.0, damagePerTier = 2.5, baseSpeed = 0.25, speedPerTier = 0.0
    )
    val WITHER_SKELETON_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = false, isSilent = false,
        baseHealth = 20.0, healthPerTier = 0.0, baseDamage = 8.0, damagePerTier = 3.0, baseSpeed = 0.25, speedPerTier = 0.0
    )
    val CREEPER_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = false,
        baseHealth = 5.0, healthPerTier = 0.0, baseDamage = 0.0, damagePerTier = 0.0, baseSpeed = 0.3, speedPerTier = 0.01
    )
    val WITCH_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = false,
        baseHealth = 15.0, healthPerTier = 0.0, baseDamage = 0.0, damagePerTier = 0.0, baseSpeed = 0.25, speedPerTier = 0.0
    )
    val PIGLIN_BRUTE_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = false, isSilent = false,
        baseHealth = 20.0, healthPerTier = 0.0, baseDamage = 5.0, damagePerTier = 3.0, baseSpeed = 0.25, speedPerTier = 0.0
    )
    val BLAZE_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = true, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = false,
        baseHealth = 10.0, healthPerTier = 0.0, baseDamage = 5.0, damagePerTier = 3.0, baseSpeed = 0.25, speedPerTier = 0.0
    )
    val MAGMA_CUBE_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = false,
        baseHealth = 15.0, healthPerTier = 0.0, baseDamage = 4.0, damagePerTier = 2.5, baseSpeed = 0.25, speedPerTier = 0.0
    )
    val SLIME_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = false,
        baseHealth = 15.0, healthPerTier = 0.0, baseDamage = 4.0, damagePerTier = 2.5, baseSpeed = 0.25, speedPerTier = 0.0
    )

    val NECROMANCER_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        baseHealth = 15.0, healthPerTier = 0.0, baseDamage = 0.0, damagePerTier = 0.0, baseSpeed = 0.4, speedPerTier = 0.0
    )
    val REAPER_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        baseHealth = 30.0, healthPerTier = 0.0, baseDamage = 7.0, damagePerTier = 5.0, baseSpeed = 0.3, speedPerTier = 0.0
    )
    val CHUPACABRA_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        baseHealth = 5.0, healthPerTier = 0.0, baseDamage = 5.0, damagePerTier = 2.5, baseSpeed = 0.35, speedPerTier = 0.0
    )
    val STALKER_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        baseHealth = 10.0, healthPerTier = 0.0, baseDamage = 4.0, damagePerTier = 1.5, baseSpeed = 0.45, speedPerTier = 0.0
    )
    val DEMONIC_GOLEM_DATA = CustomEntityData(
        canHaveWeapons = false, isRanged = false, canHaveArmor = false, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        baseHealth = 150.0, healthPerTier = 8.0, baseDamage = 20.0, damagePerTier = 3.0, baseSpeed = 0.2, speedPerTier = 0.0
    )
    val MINOTAUR_DATA = CustomEntityData(
        canHaveWeapons = false, isRanged = false, canHaveArmor = false, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        baseHealth = 100.0, healthPerTier = 5.0, baseDamage = 12.0, damagePerTier = 3.0, baseSpeed = 0.4, speedPerTier = 0.0
    )
    val NAGA_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = true, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        baseHealth = 10.0, healthPerTier = 0.0, baseDamage = 5.0, damagePerTier = 2.5, baseSpeed = 0.35, speedPerTier = 0.0
    )
    val CYCLOPS_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        baseHealth = 20.0, healthPerTier = 0.0, baseDamage = 5.0, damagePerTier = 2.5, baseSpeed = 0.25, speedPerTier = 0.0
    )
    val HARPY_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = true, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        baseHealth = 14.0, healthPerTier = 0.0, baseDamage = 7.0, damagePerTier = 3.0, baseSpeed = 0.33, speedPerTier = 0.0
    )
    val CERBERUS_DATA = CustomEntityData(
        canHaveWeapons = false, isRanged = false, canHaveArmor = false, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        baseHealth = 100.0, healthPerTier = 5.0, baseDamage = 15.0, damagePerTier = 3.5, baseSpeed = 0.35, speedPerTier = 0.0
    )
    val SUCCUBUS_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        baseHealth = 20.0, healthPerTier = 0.0, baseDamage = 7.5, damagePerTier = 5.0, baseSpeed = 0.3, speedPerTier = 0.0
    )
    val INCUBUS_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        baseHealth = 45.0, healthPerTier = 0.0, baseDamage = 5.0, damagePerTier = 3.5, baseSpeed = 0.25, speedPerTier = 0.0
    )
    val IMP_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = true, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        baseHealth = 15.0, healthPerTier = 0.0, baseDamage = 4.0, damagePerTier = 2.5, baseSpeed = 0.3, speedPerTier = 0.0
    )
    val HATCHERY_DATA = CustomEntityData(
        canHaveWeapons = false, isRanged = true, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        baseHealth = 15.0, healthPerTier = 0.0, baseDamage = 0.0, damagePerTier = 0.0, baseSpeed = 0.0, speedPerTier = 0.0
    )
    val LEECH_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        baseHealth = 5.0, healthPerTier = 0.0, baseDamage = 4.0, damagePerTier = 2.5, baseSpeed = 0.35, speedPerTier = 0.0
    )
    val MUSHROOM_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        baseHealth = 20.0, healthPerTier = 0.0, baseDamage = 5.0, damagePerTier = 2.5, baseSpeed = 0.23, speedPerTier = 0.0
    )
    val FOREST_SKELETON_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = true, canHaveArmor = true, dropBaseLoot = false, hideEquipment = false, isSilent = false,
        baseHealth = 15.0, healthPerTier = 0.0, baseDamage = 5.0, damagePerTier = 2.5, baseSpeed = 0.25, speedPerTier = 0.0
    )
    val MELEE_FOREST_SKELETON_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = false, isSilent = false,
        baseHealth = 20.0, healthPerTier = 0.0, baseDamage = 5.0, damagePerTier = 2.5, baseSpeed = 0.25, speedPerTier = 0.0
    )
    val DRYAD_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        baseHealth = 25.0, healthPerTier = 0.0, baseDamage = 6.0, damagePerTier = 3.5, baseSpeed = 0.3, speedPerTier = 0.0
    )
    val WENDIGO_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        baseHealth = 25.0, healthPerTier = 0.0, baseDamage = 7.0, damagePerTier = 4.5, baseSpeed = 0.3, speedPerTier = 0.0
    )
    val MANDRAGORA_DATA = CustomEntityData(
        canHaveWeapons = false, isRanged = false, canHaveArmor = false, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        baseHealth = 120.0, healthPerTier = 6.0, baseDamage = 12.0, damagePerTier = 3.0, baseSpeed = 0.3, speedPerTier = 0.0
    )

    val STONE_PILLAR_DATA = CustomEntityData(
        canHaveWeapons = false, isRanged = false, canHaveArmor = false, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        baseHealth = 1.0, healthPerTier = 0.0, baseDamage = 0.0, damagePerTier = 0.0, baseSpeed = 0.0, speedPerTier = 0.0
    )
    val VINE_DATA = CustomEntityData(
        canHaveWeapons = false, isRanged = false, canHaveArmor = false, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        baseHealth = 10.0, healthPerTier = 0.0, baseDamage = 5.0, damagePerTier = 2.5, baseSpeed = 0.0, speedPerTier = 0.0
    )

    val POISON_SPIT_DATA = CustomEntityData(
        canHaveWeapons = false, isRanged = false, canHaveArmor = false, dropBaseLoot = false, hideEquipment = true, isSilent = false,
        baseHealth = 1.0, healthPerTier = 0.0, baseDamage = 0.0, damagePerTier = 0.0, baseSpeed = 0.0, speedPerTier = 0.0
    )
}