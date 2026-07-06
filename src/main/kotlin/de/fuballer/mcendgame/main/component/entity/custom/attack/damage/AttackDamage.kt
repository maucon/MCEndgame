package de.fuballer.mcendgame.main.component.entity.custom.attack.damage

import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.MobEntity
import net.minecraft.server.world.ServerWorld

abstract class AttackDamage(
    private val damageFactor: Float,
    private val knockbackFactor: Double,
    val blockable: Boolean = true,
    val disableBlockingShield: Float = 0.0F,
) {
    abstract fun apply(world: ServerWorld, damager: MobEntity, target: LivingEntity?): Boolean

    fun getDamage(damager: MobEntity) = damager.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE).toFloat() * damageFactor
    fun getKnockback(damager: MobEntity) = damager.getAttributeValue(EntityAttributes.GENERIC_ATTACK_KNOCKBACK).toFloat() * knockbackFactor

    open fun requiresTarget() = true
}