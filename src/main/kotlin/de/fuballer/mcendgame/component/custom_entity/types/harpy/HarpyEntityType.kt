package de.fuballer.mcendgame.component.custom_entity.types.harpy

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import org.bukkit.entity.EntityType

object HarpyEntityType : CustomEntityType {
    override val type = EntityType.SKELETON

    override val customName = "Harpy"
    override val canHaveWeapons = true
    override val isRanged = true
    override val canHaveArmor = true
    override val hideEquipment = true
    override val isSilent = true

    override val baseHealth = 14.0
    override val healthPerTier = 0.0
    override val baseDamage = 7.0
    override val damagePerTier = 3.0
    override val baseSpeed = 0.33
    override val speedPerTier = 0.0

    override val abilities = null
}