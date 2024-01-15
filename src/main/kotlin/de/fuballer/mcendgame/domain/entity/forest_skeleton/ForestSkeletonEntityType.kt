package de.fuballer.mcendgame.domain.entity.forest_skeleton

import de.fuballer.mcendgame.domain.entity.CustomEntityType
import org.bukkit.entity.EntityType

object ForestSkeletonEntityType : CustomEntityType {
    override val type = EntityType.SKELETON

    override val customName = "Forest Skeleton"
    override val canHaveWeapons = true
    override val isRanged = true
    override val canHaveArmor = true
    override val hideEquipment = false

    override val baseHealth = 15.0
    override val healthPerTier = 0.0
    override val baseDamage = 5.0
    override val damagePerTier = 2.5
    override val baseSpeed = 0.25
    override val speedPerTier = 0.0

    override val sounds = null
    override val abilities = null
}