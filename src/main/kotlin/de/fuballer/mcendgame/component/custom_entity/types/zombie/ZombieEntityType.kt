package de.fuballer.mcendgame.component.custom_entity.types.zombie

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import org.bukkit.entity.EntityType

object ZombieEntityType : CustomEntityType {
    override val type = EntityType.ZOMBIE

    override val customName = null
    override val canHaveWeapons = true
    override val isRanged = false
    override val canHaveArmor = true
    override val hideEquipment = false

    override val baseHealth = 20.0
    override val healthPerTier = 0.0
    override val baseDamage = 5.0
    override val damagePerTier = 2.5
    override val baseSpeed = 0.23
    override val speedPerTier = 0.0
    override val knockbackResistance = 0.0

    override val sounds = null
    override val abilities = null
}