package de.fuballer.mcendgame.component.custom_entity.types.imp

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import org.bukkit.entity.EntityType

object ImpEntityType : CustomEntityType {
    override val type = EntityType.SKELETON

    override val customName = "Imp"
    override val canHaveWeapons = true
    override val isRanged = true
    override val canHaveArmor = true
    override val hideEquipment = true

    override val baseHealth = 15.0
    override val healthPerTier = 0.0
    override val baseDamage = 4.0
    override val damagePerTier = 2.5
    override val baseSpeed = 0.3
    override val speedPerTier = 0.0

    override val sounds = null
    override val abilities = null
}