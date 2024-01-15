package de.fuballer.mcendgame.domain.entity.blaze

import de.fuballer.mcendgame.domain.entity.CustomEntityType
import org.bukkit.entity.EntityType

object BlazeEntityType : CustomEntityType {
    override val type = EntityType.BLAZE

    override val customName = null
    override val canHaveWeapons = true
    override val isRanged = false
    override val canHaveArmor = true
    override val hideEquipment = true
    override val isSilent = false

    override val baseHealth = 10.0
    override val healthPerTier = 0.0
    override val baseDamage = 5.0
    override val damagePerTier = 3.0
    override val baseSpeed = 0.25
    override val speedPerTier = 0.0

    override val abilities = null
}