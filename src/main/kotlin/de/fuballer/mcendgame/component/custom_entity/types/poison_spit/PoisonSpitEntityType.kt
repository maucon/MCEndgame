package de.fuballer.mcendgame.component.custom_entity.types.poison_spit

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import org.bukkit.entity.EntityType

object PoisonSpitEntityType : CustomEntityType {
    override val type = EntityType.LLAMA_SPIT

    override val customName = "Poison Spit"
    override val canHaveWeapons = false
    override val isRanged = false
    override val canHaveArmor = false
    override val hideEquipment = true

    override val baseHealth = 1.0
    override val healthPerTier = 0.0
    override val baseDamage = 0.0
    override val damagePerTier = 0.0
    override val baseSpeed = 0.0
    override val speedPerTier = 0.0

    override val sounds = null
    override val abilities = null
}