package de.fuballer.mcendgame.component.custom_entity.types.necromancer

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import org.bukkit.entity.EntityType

object NecromancerEntityType : CustomEntityType {
    override val type = EntityType.EVOKER

    override val customName = "Necromancer"
    override val canHaveWeapons = true
    override val isRanged = false
    override val canHaveArmor = true
    override val hideEquipment = true

    override val baseHealth = 15.0
    override val healthPerTier = 0.0
    override val baseDamage = 0.0
    override val damagePerTier = 0.0
    override val baseSpeed = 0.4
    override val speedPerTier = 0.0

    override val sounds = null
    override val abilities = null
}