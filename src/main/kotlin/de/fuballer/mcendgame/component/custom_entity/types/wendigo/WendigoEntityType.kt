package de.fuballer.mcendgame.component.custom_entity.types.wendigo

import de.fuballer.mcendgame.component.custom_entity.EntitySoundData
import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import org.bukkit.entity.EntityType

object WendigoEntityType : CustomEntityType {
    override val type = EntityType.WITHER_SKELETON

    override val customName = "Wendigo"
    override val canHaveWeapons = false
    override val isRanged = false
    override val canHaveArmor = false
    override val hideEquipment = true

    override val baseHealth = 100.0
    override val healthPerTier = 5.0
    override val baseDamage = 25.0
    override val damagePerTier = 4.0
    override val baseSpeed = 0.35
    override val speedPerTier = 0.005

    override val sounds = EntitySoundData.create("wendigo")
    override val abilities = null
}