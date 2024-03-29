package de.fuballer.mcendgame.component.custom_entity.types.buff_villager

import de.fuballer.mcendgame.component.custom_entity.EntitySoundData
import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import org.bukkit.entity.EntityType

object BuffVillagerEntityType : CustomEntityType {
    override val type = EntityType.WITHER_SKELETON

    override val customName = "Buff Villager"
    override val canHaveWeapons = true
    override val isRanged = false
    override val canHaveArmor = true
    override val hideEquipment = true

    override val baseHealth = 20.0
    override val healthPerTier = 0.0
    override val baseDamage = 5.0
    override val damagePerTier = 2.5
    override val baseSpeed = 0.26
    override val speedPerTier = 0.0

    override val sounds = EntitySoundData.create("buff_villager")
    override val abilities = null
}