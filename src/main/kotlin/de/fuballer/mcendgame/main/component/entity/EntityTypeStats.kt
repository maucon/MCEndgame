package de.fuballer.mcendgame.main.component.entity

import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob

interface EntityTypeStats {
    val type: EntityType<out Mob>

    val equipmentClass: EnemyEquipmentClass
    val canBeInvisible: Boolean

    val health: Double
    val attackDamage: Double
    val spellDamage: Double
        get() = 0.0
    val movementSpeed: Double
    val knockbackResistance: Double

    fun applyMisc(mob: Mob) {}
}