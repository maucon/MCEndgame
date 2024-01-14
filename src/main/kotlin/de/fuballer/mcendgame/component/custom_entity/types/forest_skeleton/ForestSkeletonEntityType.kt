package de.fuballer.mcendgame.component.custom_entity.types.forest_skeleton

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import org.bukkit.entity.EntityType

object ForestSkeletonEntityType : CustomEntityType {
    override val type = EntityType.SKELETON

    override val customName = "Forest Skeleton"
    override val canHaveWeapons = true
    override val isRanged = true
    override val canHaveArmor = true
    override val hideEquipment = false
    override val isSilent = false

    override val baseHealth = 15.0
    override val healthPerTier = 0.0
    override val baseDamage = 5.0
    override val damagePerTier = 2.5
    override val baseSpeed = 0.25
    override val speedPerTier = 0.0

    override val abilities = null
}