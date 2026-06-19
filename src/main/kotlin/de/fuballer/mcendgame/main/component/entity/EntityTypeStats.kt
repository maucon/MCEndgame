package de.fuballer.mcendgame.main.component.entity

import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob

interface EntityTypeStats {
    val type: EntityType<out Mob>

    val canHaveWeapons: Boolean
    val isRanged: Boolean
    val canHaveArmor: Boolean
    val canBeInvisible: Boolean

    val health: Double
    val attackDamage: Double
    val elementalDamage: Double
        get() = 0.0
    val movementSpeed: Double
    val knockbackResistance: Double

    fun applyMisc(entity: Entity) {}
}