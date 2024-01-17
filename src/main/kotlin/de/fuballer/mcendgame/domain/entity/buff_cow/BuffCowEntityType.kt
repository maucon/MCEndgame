package de.fuballer.mcendgame.domain.entity.buff_cow

import de.fuballer.mcendgame.domain.entity.CustomEntityType
import org.bukkit.entity.EntityType

object BuffCowEntityType : CustomEntityType {
    override val type = EntityType.WITHER_SKELETON

    override val customName = "Buff Cow"
    override val canHaveWeapons = true
    override val isRanged = false
    override val canHaveArmor = true
    override val hideEquipment = true

    override val baseHealth = 25.0
    override val healthPerTier = 0.0
    override val baseDamage = 4.0
    override val damagePerTier = 2.0
    override val baseSpeed = 0.22
    override val speedPerTier = 0.0

    override val abilities = null
    override val sounds = null
}