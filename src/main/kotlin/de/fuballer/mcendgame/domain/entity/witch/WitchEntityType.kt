package de.fuballer.mcendgame.domain.entity.witch

import de.fuballer.mcendgame.domain.entity.CustomEntityType
import org.bukkit.entity.EntityType

object WitchEntityType : CustomEntityType {
    override val type = EntityType.WITCH

    override val customName = null
    override val canHaveWeapons = true
    override val isRanged = false
    override val canHaveArmor = true
    override val hideEquipment = true

    override val baseHealth = 15.0
    override val healthPerTier = 0.0
    override val baseDamage = 0.0
    override val damagePerTier = 0.0
    override val baseSpeed = 0.25
    override val speedPerTier = 0.0

    override val sounds = null
    override val abilities = null
}