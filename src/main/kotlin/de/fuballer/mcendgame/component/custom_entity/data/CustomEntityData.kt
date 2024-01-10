package de.fuballer.mcendgame.component.custom_entity.data

data class CustomEntityData(
    val canHaveWeapons: Boolean,
    val isRanged: Boolean,
    val canHaveArmor: Boolean,
    val dropBaseLoot: Boolean,
    val hideEquipment: Boolean,
    val isSilent: Boolean,

    val baseHealth: Double,
    val healthPerTier: Double,
    val baseDamage: Double,
    val damagePerTier: Double,
    val baseSpeed: Double,
    val speedPerTier: Double,
)