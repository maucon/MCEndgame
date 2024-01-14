package de.fuballer.mcendgame.component.custom_entity.types.piglin_brute

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import org.bukkit.entity.EntityType

object PiglinBruteEntityType : CustomEntityType {
    override val type = EntityType.PIGLIN_BRUTE

    override val customName = null
    override val canHaveWeapons = true
    override val isRanged = false
    override val canHaveArmor = true
    override val hideEquipment = false
    override val isSilent = false

    override val baseHealth = 20.0
    override val healthPerTier = 0.0
    override val baseDamage = 5.0
    override val damagePerTier = 3.0
    override val baseSpeed = 0.25
    override val speedPerTier = 0.0

    override val abilities = null
}