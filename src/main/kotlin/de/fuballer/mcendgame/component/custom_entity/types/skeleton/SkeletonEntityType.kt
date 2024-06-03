package de.fuballer.mcendgame.component.custom_entity.types.skeleton

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import org.bukkit.entity.EntityType

object SkeletonEntityType : CustomEntityType {
    override val type = EntityType.SKELETON

    override val customName = null
    override val canHaveWeapons = true
    override val isRanged = true
    override val canHaveArmor = true
    override val hideEquipment = false

    override val baseHealth = 15.0
    override val healthPerTier = 0.0
    override val baseDamage = 0.0
    override val damagePerTier = 1.2
    override val baseSpeed = 0.25
    override val speedPerTier = 0.0

    override val sounds = null
    override val abilities = null
}