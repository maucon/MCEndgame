package de.fuballer.mcendgame.component.custom_entity.types.mushroom

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import org.bukkit.entity.EntityType

object MushroomEntityType : CustomEntityType {
    override val type = EntityType.ZOMBIE

    override val customName = "Mushroom"
    override val canHaveWeapons = true
    override val isRanged = false
    override val canHaveArmor = true
    override val hideEquipment = true

    override val baseHealth = 20.0
    override val healthPerTier = 0.0
    override val baseDamage = 5.0
    override val damagePerTier = 2.5
    override val baseSpeed = 0.23
    override val speedPerTier = 0.0

    override val sounds = null
    override val abilities = null
}