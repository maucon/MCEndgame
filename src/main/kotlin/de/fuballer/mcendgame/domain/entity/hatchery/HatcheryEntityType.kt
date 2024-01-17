package de.fuballer.mcendgame.domain.entity.hatchery

import de.fuballer.mcendgame.domain.entity.CustomEntityType
import org.bukkit.entity.EntityType

object HatcheryEntityType : CustomEntityType {
    override val type = EntityType.SKELETON

    override val customName = "Hatchery"
    override val canHaveWeapons = false
    override val isRanged = true
    override val canHaveArmor = true
    override val hideEquipment = true

    override val baseHealth = 15.0
    override val healthPerTier = 0.0
    override val baseDamage = 0.0
    override val damagePerTier = 0.0
    override val baseSpeed = 0.0
    override val speedPerTier = 0.0

    override val sounds = null
    override val abilities = null
}