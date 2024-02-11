package de.fuballer.mcendgame.component.custom_entity.types.vine

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import org.bukkit.entity.EntityType

object VineEntityType : CustomEntityType {
    override val type = EntityType.ZOMBIE

    override val customName = "Vine"
    override val canHaveWeapons = false
    override val isRanged = false
    override val canHaveArmor = false
    override val hideEquipment = true

    override val baseHealth = 10.0
    override val healthPerTier = 0.0
    override val baseDamage = 5.0
    override val damagePerTier = 2.5
    override val baseSpeed = 0.0
    override val speedPerTier = 0.0

    override val sounds = null
    override val abilities = null
}