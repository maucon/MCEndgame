package de.fuballer.mcendgame.component.custom_entity.types.magma_cube

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import org.bukkit.entity.EntityType

object MagmaCubeEntityType : CustomEntityType {
    override val type = EntityType.MAGMA_CUBE

    override val customName = null
    override val canHaveWeapons = true
    override val isRanged = false
    override val canHaveArmor = true
    override val hideEquipment = true

    override val baseHealth = 15.0
    override val healthPerTier = 0.0
    override val baseDamage = 4.0
    override val damagePerTier = 2.5
    override val baseSpeed = 0.25
    override val speedPerTier = 0.0

    override val sounds = null
    override val abilities = null
}