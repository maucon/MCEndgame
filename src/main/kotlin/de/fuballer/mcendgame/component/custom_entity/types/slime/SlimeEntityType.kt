package de.fuballer.mcendgame.component.custom_entity.types.slime

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import org.bukkit.entity.EntityType

object SlimeEntityType : CustomEntityType {
    override val type = EntityType.SLIME

    override val customName = null
    override val canHaveWeapons = true
    override val isRanged = false
    override val canHaveArmor = true
    override val dropBaseLoot = false
    override val hideEquipment = true
    override val isSilent = false

    override val baseHealth = 15.0
    override val healthPerTier = 0.0
    override val baseDamage = 4.0
    override val damagePerTier = 2.5
    override val baseSpeed = 0.25
    override val speedPerTier = 0.0

    override val abilities = null
}