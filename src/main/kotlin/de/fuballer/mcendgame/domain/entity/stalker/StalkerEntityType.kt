package de.fuballer.mcendgame.domain.entity.stalker

import de.fuballer.mcendgame.domain.entity.CustomEntityType
import org.bukkit.entity.EntityType

object StalkerEntityType : CustomEntityType {
    override val type = EntityType.ZOMBIE

    override val customName = "Stalker"
    override val canHaveWeapons = true
    override val isRanged = false
    override val canHaveArmor = true
    override val hideEquipment = true
    override val isSilent = true

    override val baseHealth = 10.0
    override val healthPerTier = 0.0
    override val baseDamage = 4.0
    override val damagePerTier = 1.5
    override val baseSpeed = 0.45
    override val speedPerTier = 0.0

    override val abilities = null
}