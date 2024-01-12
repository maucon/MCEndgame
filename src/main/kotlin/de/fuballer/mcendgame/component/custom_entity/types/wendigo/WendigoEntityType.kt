package de.fuballer.mcendgame.component.custom_entity.types.wendigo

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import org.bukkit.entity.EntityType

object WendigoEntityType : CustomEntityType {
    override val type = EntityType.WITHER_SKELETON

    override val customName = "Wendigo"
    override val canHaveWeapons = true
    override val isRanged = false
    override val canHaveArmor = true
    override val dropBaseLoot = false
    override val hideEquipment = true
    override val isSilent = true

    override val baseHealth = 25.0
    override val healthPerTier = 0.0
    override val baseDamage = 7.0
    override val damagePerTier = 4.5
    override val baseSpeed = 0.3
    override val speedPerTier = 0.0

    override val abilities = null
}