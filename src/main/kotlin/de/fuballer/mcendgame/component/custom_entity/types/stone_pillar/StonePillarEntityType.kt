package de.fuballer.mcendgame.component.custom_entity.types.stone_pillar

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import org.bukkit.entity.EntityType

object StonePillarEntityType : CustomEntityType {
    override val type = EntityType.ZOMBIE

    override val customName = "Stone Pillar"
    override val canHaveWeapons = false
    override val isRanged = false
    override val canHaveArmor = false
    override val dropBaseLoot = false
    override val hideEquipment = true
    override val isSilent = true

    override val baseHealth = 1.0
    override val healthPerTier = 0.0
    override val baseDamage = 0.0
    override val damagePerTier = 0.0
    override val baseSpeed = 0.0
    override val speedPerTier = 0.0

    override val abilities = null
}