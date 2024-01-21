package de.fuballer.mcendgame.domain.entity.reaper

import de.fuballer.mcendgame.component.custom_entity.EntitySoundData
import de.fuballer.mcendgame.domain.entity.CustomEntityType
import org.bukkit.entity.EntityType

object ReaperEntityType : CustomEntityType {
    override val type = EntityType.WITHER_SKELETON

    override val customName = "Reaper"
    override val canHaveWeapons = true
    override val isRanged = false
    override val canHaveArmor = true
    override val hideEquipment = true

    override val baseHealth = 30.0
    override val healthPerTier = 0.0
    override val baseDamage = 7.0
    override val damagePerTier = 5.0
    override val baseSpeed = 0.3
    override val speedPerTier = 0.0

    override val sounds = EntitySoundData.create("reaper")
    override val abilities = null
}