package de.fuballer.mcendgame.component.custom_entity.types.buff_allay

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import org.bukkit.entity.EntityType

object BuffAllayEntityType : CustomEntityType {
    override val type = EntityType.SKELETON

    override val customName = "Buff Allay"
    override val canHaveWeapons = true
    override val isRanged = true
    override val canHaveArmor = true
    override val hideEquipment = true

    override val baseHealth = 12.0
    override val healthPerTier = 0.0
    override val baseDamage = 5.0
    override val damagePerTier = 3.0
    override val baseSpeed = 0.25
    override val speedPerTier = 0.0

    override val sounds = null
    override val abilities = null
}