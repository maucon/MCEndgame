package de.fuballer.mcendgame.component.custom_entity

import de.fuballer.mcendgame.component.custom_entity.data.CustomEntityData

object CustomEntitySettings {
    val ZOMBIE_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = false, isSilent = false,
        maxLifeBase = 20.0, maxLifePerTier = 0.0, damageBase = 5.0, damagePerTier = 2.5, speedBase = 0.23, speedPerTier = 0.0
    )
    val HUSK_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = false, isSilent = false,
        maxLifeBase = 25.0, maxLifePerTier = 0.0, damageBase = 5.0, damagePerTier = 2.5, speedBase = 0.23, speedPerTier = 0.0
    )
    val SKELETON_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = true, canHaveArmor = true, dropBaseLoot = false, hideEquipment = false, isSilent = false,
        maxLifeBase = 15.0, maxLifePerTier = 0.0, damageBase = 5.0, damagePerTier = 2.5, speedBase = 0.25, speedPerTier = 0.0
    )
    val STRAY_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = true, canHaveArmor = true, dropBaseLoot = false, hideEquipment = false, isSilent = false,
        maxLifeBase = 15.0, maxLifePerTier = 0.0, damageBase = 5.0, damagePerTier = 2.5, speedBase = 0.25, speedPerTier = 0.0
    )
    val WITHER_SKELETON_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = false, isSilent = false,
        maxLifeBase = 20.0, maxLifePerTier = 0.0, damageBase = 8.0, damagePerTier = 3.0, speedBase = 0.25, speedPerTier = 0.0
    )
    val CREEPER_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = false,
        maxLifeBase = 5.0, maxLifePerTier = 0.0, damageBase = 0.0, damagePerTier = 0.0, speedBase = 0.3, speedPerTier = 0.01
    )
    val WITCH_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = false,
        maxLifeBase = 15.0, maxLifePerTier = 0.0, damageBase = 0.0, damagePerTier = 0.0, speedBase = 0.25, speedPerTier = 0.0
    )
    val PIGLIN_BRUTE_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = false, isSilent = false,
        maxLifeBase = 20.0, maxLifePerTier = 0.0, damageBase = 5.0, damagePerTier = 3.0, speedBase = 0.25, speedPerTier = 0.0
    )
    val BLAZE_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = true, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = false,
        maxLifeBase = 10.0, maxLifePerTier = 0.0, damageBase = 5.0, damagePerTier = 3.0, speedBase = 0.25, speedPerTier = 0.0
    )
    val MAGMA_CUBE_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = false,
        maxLifeBase = 15.0, maxLifePerTier = 0.0, damageBase = 4.0, damagePerTier = 2.5, speedBase = 0.25, speedPerTier = 0.0
    )

    val NECROMANCER_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        maxLifeBase = 15.0, maxLifePerTier = 0.0, damageBase = 0.0, damagePerTier = 0.0, speedBase = 0.4, speedPerTier = 0.0
    )
    val REAPER_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        maxLifeBase = 20.0, maxLifePerTier = 0.0, damageBase = 10.0, damagePerTier = 3.5, speedBase = 0.3, speedPerTier = 0.0
    )
    val CHUPACABRA_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        maxLifeBase = 5.0, maxLifePerTier = 0.0, damageBase = 5.0, damagePerTier = 2.5, speedBase = 0.35, speedPerTier = 0.0
    )
    val STALKER_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        maxLifeBase = 10.0, maxLifePerTier = 0.0, damageBase = 4.0, damagePerTier = 1.5, speedBase = 0.45, speedPerTier = 0.0
    )
    val DEMONIC_GOLEM_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        maxLifeBase = 1.0, maxLifePerTier = 0.0, damageBase = 0.0, damagePerTier = 0.0, speedBase = 0.0, speedPerTier = 0.0
    )
    val MINOTAUR_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        maxLifeBase = 1.0, maxLifePerTier = 0.0, damageBase = 0.0, damagePerTier = 0.0, speedBase = 0.0, speedPerTier = 0.0
    )
    val NAGA_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = true, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        maxLifeBase = 10.0, maxLifePerTier = 0.0, damageBase = 5.0, damagePerTier = 2.5, speedBase = 0.35, speedPerTier = 0.0
    )
    val CYCLOPS_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        maxLifeBase = 20.0, maxLifePerTier = 0.0, damageBase = 5.0, damagePerTier = 2.5, speedBase = 0.25, speedPerTier = 0.0
    )
    val HARPY_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = true, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        maxLifeBase = 14.0, maxLifePerTier = 0.0, damageBase = 7.0, damagePerTier = 3.0, speedBase = 0.33, speedPerTier = 0.0
    )
    val CERBERUS_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        maxLifeBase = 1.0, maxLifePerTier = 0.0, damageBase = 0.0, damagePerTier = 0.0, speedBase = 0.0, speedPerTier = 0.0
    )
    val SUCCUBUS_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        maxLifeBase = 15.0, maxLifePerTier = 0.0, damageBase = 7.0, damagePerTier = 3.0, speedBase = 0.3, speedPerTier = 0.0
    )
    val INCUBUS_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        maxLifeBase = 22.0, maxLifePerTier = 0.0, damageBase = 3.0, damagePerTier = 2.0, speedBase = 0.25, speedPerTier = 0.0
    )
    val IMP_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = true, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        maxLifeBase = 15.0, maxLifePerTier = 0.0, damageBase = 4.0, damagePerTier = 2.5, speedBase = 0.3, speedPerTier = 0.0
    )
    val HATCHERY_DATA = CustomEntityData(
        canHaveWeapons = false, isRanged = true, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        maxLifeBase = 15.0, maxLifePerTier = 0.0, damageBase = 0.0, damagePerTier = 0.0, speedBase = 0.0, speedPerTier = 0.0
    )
    val LEECH_DATA = CustomEntityData(
        canHaveWeapons = true, isRanged = false, canHaveArmor = true, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        maxLifeBase = 5.0, maxLifePerTier = 0.0, damageBase = 4.0, damagePerTier = 2.5, speedBase = 0.35, speedPerTier = 0.0
    )

    val STONE_PILLAR_DATA = CustomEntityData(
        canHaveWeapons = false, isRanged = false, canHaveArmor = false, dropBaseLoot = false, hideEquipment = true, isSilent = true,
        maxLifeBase = 1.0, maxLifePerTier = 0.0, damageBase = 0.0, damagePerTier = 0.0, speedBase = 0.0, speedPerTier = 0.0
    )

    val POISON_SPIT_DATA = CustomEntityData(
        canHaveWeapons = false, isRanged = false, canHaveArmor = false, dropBaseLoot = false, hideEquipment = true, isSilent = false,
        maxLifeBase = 1.0, maxLifePerTier = 0.0, damageBase = 0.0, damagePerTier = 0.0, speedBase = 0.0, speedPerTier = 0.0
    )
}