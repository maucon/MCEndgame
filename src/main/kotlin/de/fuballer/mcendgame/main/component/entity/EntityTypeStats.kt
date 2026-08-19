package de.fuballer.mcendgame.main.component.entity

import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import kotlin.math.pow
import kotlin.random.Random

interface EntityTypeStats {
    val type: EntityType<out Mob>

    val equipmentClass: EnemyEquipmentClass
    val canBeInvisible: Boolean
    val canHaveEffects: Boolean
        get() = true

    val health: Double
    val attackDamage: Double
    val spellDamage: Double
        get() = 0.0
    val movementSpeed: Double
    val knockbackResistance: Double

    fun getRandomScale(random: Random): Double = 1.0 + 0.2 * random.nextDouble().pow(3) * if (random.nextBoolean()) 1 else -1

    fun applyMisc(entity: LivingEntity) {}
}