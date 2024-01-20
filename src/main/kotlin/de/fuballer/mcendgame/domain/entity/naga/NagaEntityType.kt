package de.fuballer.mcendgame.domain.entity.naga

import de.fuballer.mcendgame.component.custom_entity.EntitySoundData
import de.fuballer.mcendgame.domain.entity.CustomEntityType
import org.bukkit.entity.EntityType

object NagaEntityType : CustomEntityType {
    override val type = EntityType.SKELETON

    override val customName = "Naga"
    override val canHaveWeapons = true
    override val isRanged = true
    override val canHaveArmor = true
    override val hideEquipment = true

    override val baseHealth = 10.0
    override val healthPerTier = 0.0
    override val baseDamage = 5.0
    override val damagePerTier = 2.5
    override val baseSpeed = 0.35
    override val speedPerTier = 0.0

    override val sounds = EntitySoundData.create("naga")
    override val abilities = null
}