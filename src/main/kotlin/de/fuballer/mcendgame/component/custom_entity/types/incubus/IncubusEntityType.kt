package de.fuballer.mcendgame.component.custom_entity.types.incubus

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import org.bukkit.entity.EntityType

object IncubusEntityType : CustomEntityType {
    override val type = EntityType.ZOMBIE

    override val customName = "Incubus"
    override val canHaveWeapons = true
    override val isRanged = false
    override val canHaveArmor = true
    override val dropBaseLoot = false
    override val hideEquipment = true
    override val isSilent = true

    override val baseHealth = 45.0
    override val healthPerTier = 0.0
    override val baseDamage = 5.0
    override val damagePerTier = 3.5
    override val baseSpeed = 0.25
    override val speedPerTier = 0.0

    override val abilities = null
}