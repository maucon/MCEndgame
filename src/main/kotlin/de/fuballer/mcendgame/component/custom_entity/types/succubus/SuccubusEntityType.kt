package de.fuballer.mcendgame.component.custom_entity.types.succubus

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import org.bukkit.entity.EntityType

object SuccubusEntityType : CustomEntityType {
    override val type = EntityType.ZOMBIE

    override val customName = "Succubus"
    override val canHaveWeapons = true
    override val isRanged = false
    override val canHaveArmor = true
    override val hideEquipment = true

    override val baseHealth = 20.0
    override val healthPerTier = 0.0
    override val baseDamage = 7.5
    override val damagePerTier = 5.0
    override val baseSpeed = 0.3
    override val speedPerTier = 0.0

    override val sounds = null
    override val abilities = null
}