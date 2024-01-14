package de.fuballer.mcendgame.component.custom_entity.types.dryad

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import org.bukkit.entity.EntityType

object DryadEntityType : CustomEntityType {
    override val type = EntityType.ZOMBIE

    override val customName = "Dryad"
    override val canHaveWeapons = true
    override val isRanged = false
    override val canHaveArmor = true
    override val hideEquipment = true
    override val isSilent = true

    override val baseHealth = 25.0
    override val healthPerTier = 0.0
    override val baseDamage = 6.0
    override val damagePerTier = 3.5
    override val baseSpeed = 0.3
    override val speedPerTier = 0.0

    override val abilities = null
}