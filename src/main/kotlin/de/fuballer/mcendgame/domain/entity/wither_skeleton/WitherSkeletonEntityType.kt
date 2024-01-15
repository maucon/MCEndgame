package de.fuballer.mcendgame.domain.entity.wither_skeleton

import de.fuballer.mcendgame.domain.entity.CustomEntityType
import org.bukkit.entity.EntityType

object WitherSkeletonEntityType : CustomEntityType {
    override val type = EntityType.WITHER_SKELETON

    override val customName = null
    override val canHaveWeapons = true
    override val isRanged = false
    override val canHaveArmor = true
    override val hideEquipment = false
    override val isSilent = false

    override val baseHealth = 20.0
    override val healthPerTier = 0.0
    override val baseDamage = 8.0
    override val damagePerTier = 3.0
    override val baseSpeed = 0.25
    override val speedPerTier = 0.0

    override val abilities = null
}