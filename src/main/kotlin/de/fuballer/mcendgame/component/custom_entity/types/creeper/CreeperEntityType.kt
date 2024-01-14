package de.fuballer.mcendgame.component.custom_entity.types.creeper

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import org.bukkit.entity.EntityType

object CreeperEntityType : CustomEntityType {
    override val type = EntityType.CREEPER

    override val customName = null
    override val canHaveWeapons = true
    override val isRanged = false
    override val canHaveArmor = true
    override val hideEquipment = false
    override val isSilent = false

    override val baseHealth = 5.0
    override val healthPerTier = 0.0
    override val baseDamage = 0.0
    override val damagePerTier = 0.0
    override val baseSpeed = 0.3
    override val speedPerTier = 0.01

    override val abilities = null
}