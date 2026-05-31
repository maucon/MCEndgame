package de.fuballer.mcendgame.main.component.damage

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttributeType
import de.fuballer.mcendgame.main.component.damage.dealing.ExtendedDamageSource
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.wasLastAttackCritical
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.entity.projectile.ProjectileEntity
import net.minecraft.server.world.ServerWorld

data class DamageCalculationCommand(
    val damager: Entity?,
    val damagerAttributes: Map<CustomAttributeType, List<CustomAttribute>>,

    val damaged: LivingEntity,
    val damagedAttributes: Map<CustomAttributeType, List<CustomAttribute>>,

    val type: DamageType,
    val world: ServerWorld,
    val isProjectile: Boolean,
    val isShieldBlocking: Boolean,
    val isCritical: Boolean,

    // === custom properties ===
    val increasedDamage: MutableList<Double> = mutableListOf(),
    val moreDamage: MutableList<Double> = mutableListOf(),

    val attackDamage: MutableList<Double> = mutableListOf(),
    val increasedAttackDamage: MutableList<Double> = mutableListOf(),
    val moreAttackDamage: MutableList<Double> = mutableListOf(),

    val elementalDamage: MutableList<Double> = mutableListOf(),
    val increasedElementalDamage: MutableList<Double> = mutableListOf(),
    val moreElementalDamage: MutableList<Double> = mutableListOf(),

    val increasedDamageTaken: MutableList<Double> = mutableListOf(),
    val moreDamageTaken: MutableList<Double> = mutableListOf(),
    val ward: MutableList<Double> = mutableListOf(),

    val criticalDamageMulti: MutableList<Double> = mutableListOf(),
    val applyCritToElementalDamage: Boolean = false,
) {
    companion object {
        fun of(
            damaged: LivingEntity,
            world: ServerWorld,
            source: ExtendedDamageSource,
            attackAttributes: List<CustomAttribute>,
            shieldBlocking: Boolean,
        ): DamageCalculationCommand {
            val damager = source.attacker
            val damagerAttributes = (damager as? LivingEntity)
                ?.getAllCustomAttributes()
                ?.toMutableMap()
                ?: mutableMapOf()
            attackAttributes.filter { it.type is CustomAttributeType }
                .groupBy { it.type as CustomAttributeType }
                .forEach { (type, attributes) ->
                    val existing = damagerAttributes[type] ?: emptyList()
                    damagerAttributes[type] = existing + attributes
                }

            val damagedAttributes = damaged.getAllCustomAttributes()
            val damageType = source.type

            val isProjectile = source.source is ProjectileEntity
            val isProjectileCritical = (source.source as? PersistentProjectileEntity)?.isCritical ?: false
            val playerCriticalAttack = (damager as? PlayerEntity)?.wasLastAttackCritical() ?: false
            val isCritical = if (isProjectile) isProjectileCritical else playerCriticalAttack

            return DamageCalculationCommand(damager, damagerAttributes, damaged, damagedAttributes, damageType, world, isProjectile, shieldBlocking, isCritical)
        }
    }
}