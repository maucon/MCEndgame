package de.fuballer.mcendgame.component.custom_entity.types.buff_witch

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import org.bukkit.entity.EntityType

object BuffWitchEntityType : CustomEntityType {
    override val type = EntityType.WITHER_SKELETON

    override val customName = "Buff Witch"
    override val canHaveWeapons = true
    override val isRanged = false
    override val canHaveArmor = true
    override val dropBaseLoot = false
    override val hideEquipment = true
    override val isSilent = true

    override val baseHealth = 15.0
    override val healthPerTier = 0.0
    override val baseDamage = 6.0
    override val damagePerTier = 3.0
    override val baseSpeed = 0.3
    override val speedPerTier = 0.0

    override val abilities = null
}