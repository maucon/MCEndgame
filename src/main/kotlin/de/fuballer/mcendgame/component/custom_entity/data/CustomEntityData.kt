package de.fuballer.mcendgame.component.custom_entity.data

data class CustomEntityData(
    val canHaveWeapons: Boolean,
    val isRanged: Boolean,
    val canHaveArmor: Boolean,
    val dropBaseLoot: Boolean,
    val hideEquipment: Boolean,
    val isSilent: Boolean,

    val maxLifeBase: Double,
    val maxLifePerTier: Double,
    val damageBase: Double,
    val damagePerTier: Double,
    val speedBase: Double,
    val speedPerTier: Double,
)