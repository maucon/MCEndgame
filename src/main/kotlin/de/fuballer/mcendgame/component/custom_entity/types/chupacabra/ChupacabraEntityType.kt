package de.fuballer.mcendgame.component.custom_entity.types.chupacabra

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import org.bukkit.entity.EntityType

object ChupacabraEntityType : CustomEntityType {
    override val type = EntityType.WOLF

    override val customName = "Chupacabra"
    override val canHaveWeapons = true
    override val isRanged = false
    override val canHaveArmor = true
    override val hideEquipment = true
    override val isSilent = true

    override val baseHealth = 5.0
    override val healthPerTier = 0.0
    override val baseDamage = 5.0
    override val damagePerTier = 2.5
    override val baseSpeed = 0.35
    override val speedPerTier = 0.0

    override val abilities = null
}