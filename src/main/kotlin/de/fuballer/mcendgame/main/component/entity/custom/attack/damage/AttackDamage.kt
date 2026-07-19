package de.fuballer.mcendgame.main.component.entity.custom.attack.damage

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.attributes.Attributes

abstract class AttackDamage(
    private val damageFactor: Float,
    private val knockbackFactor: Double,
    val blockable: Boolean = true,
    val disableBlockingShield: Float = 0.0F,
    val knockbackWhenBlocked : Boolean = false,
) {
    abstract fun apply(world: ServerLevel, damager: Mob, target: LivingEntity?): Boolean

    fun getDamage(damager: Mob) = damager.getAttributeValue(Attributes.ATTACK_DAMAGE).toFloat() * damageFactor
    fun getKnockback(damager: Mob) = damager.getAttributeValue(Attributes.ATTACK_KNOCKBACK).toFloat() * knockbackFactor

    open fun requiresTarget() = true
}