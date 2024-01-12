package de.fuballer.mcendgame.component.custom_entity.types.stalker

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import org.bukkit.entity.EntityType

object StalkerEntityType : CustomEntityType {
    override val type = EntityType.ZOMBIE

    override val customName = "Stalker"
    override val canHaveWeapons = true
    override val isRanged = false
    override val canHaveArmor = true
    override val dropBaseLoot = false
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