package de.fuballer.mcendgame.component.custom_entity.types.leech

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import org.bukkit.entity.EntityType

object LeechEntityType : CustomEntityType {
    override val type = EntityType.BEE

    override val customName = "Leech"
    override val canHaveWeapons = true
    override val isRanged = false
    override val canHaveArmor = true
    override val dropBaseLoot = false
    override val hideEquipment = true
    override val isSilent = true

    override val baseHealth = 5.0
    override val healthPerTier = 0.0
    override val baseDamage = 4.0
    override val damagePerTier = 2.5
    override val baseSpeed = 0.35
    override val speedPerTier = 0.0

    override val abilities = null
}