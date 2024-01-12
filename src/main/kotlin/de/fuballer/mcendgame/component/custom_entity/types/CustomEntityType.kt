package de.fuballer.mcendgame.component.custom_entity.types

import de.fuballer.mcendgame.component.custom_entity.ability.Ability
import de.fuballer.mcendgame.util.random.RandomOption
import org.bukkit.entity.EntityType

interface CustomEntityType {
    val type: EntityType

    val customName: String?
    val canHaveWeapons: Boolean
    val isRanged: Boolean
    val canHaveArmor: Boolean
    val dropBaseLoot: Boolean
    val hideEquipment: Boolean
    val isSilent: Boolean

    val baseHealth: Double
    val healthPerTier: Double
    val baseDamage: Double
    val damagePerTier: Double
    val baseSpeed: Double
    val speedPerTier: Double

    val abilities: List<RandomOption<Ability>>?
}