package de.fuballer.mcendgame.domain.entity.imp

import de.fuballer.mcendgame.domain.entity.CustomEntityType
import org.bukkit.entity.EntityType

object ImpEntityType : CustomEntityType {
    override val type = EntityType.SKELETON

    override val customName = "Imp"
    override val canHaveWeapons = true
    override val isRanged = true
    override val canHaveArmor = true
    override val hideEquipment = true
    override val isSilent = true

    override val baseHealth = 15.0
    override val healthPerTier = 0.0
    override val baseDamage = 4.0
    override val damagePerTier = 2.5
    override val baseSpeed = 0.3
    override val speedPerTier = 0.0

    override val abilities = null
}