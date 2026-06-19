package de.fuballer.mcendgame.main.component.damage

import de.fuballer.mcendgame.main.component.custom_attribute.CustomAttributesExtensions.getAllCustomAttributes
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttributeType
import de.fuballer.mcendgame.main.component.damage.dealing.ExtendedDamageSource
import de.fuballer.mcendgame.main.util.extension.mixin.PlayerEntityMixinExtension.wasLastAttackCritical
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.Projectile
import net.minecraft.world.entity.projectile.arrow.AbstractArrow

data class DamageCalculationCommand(
    val damager: Entity?,
    val damagerAttributes: Map<CustomAttributeType, List<CustomAttribute>>,

    val damaged: LivingEntity,
    val damagedAttributes: Map<CustomAttributeType, List<CustomAttribute>>,

    val type: DamageType,
    val world: ServerLevel,
    val isProjectile: Boolean,
    val isShieldBlocking: Boolean,
    val isCritical: Boolean,

    // === custom properties ===
    val increasedDamage: MutableList<Double> = mutableListOf(),
    val moreDamage: MutableList<Double> = mutableListOf(),

    val attackDamage: MutableList<Double> = mutableListOf(),
    val increasedAttackDamage: MutableList<Double> = mutableListOf(),
    val moreAttackDamage: MutableList<Double> = mutableListOf(),

    val spellDamage: MutableList<Double> = mutableListOf(),
    val increasedSpellDamage: MutableList<Double> = mutableListOf(),
    val moreSpellDamage: MutableList<Double> = mutableListOf(),

    val increasedDamageTaken: MutableList<Double> = mutableListOf(),
    val moreDamageTaken: MutableList<Double> = mutableListOf(),
    val ward: MutableList<Double> = mutableListOf(),

    val criticalDamageMulti: MutableList<Double> = mutableListOf(),
    val applyCritToSpellDamage: Boolean = false,
) {
    companion object {
        fun of(
            damaged: LivingEntity,
            world: ServerLevel,
            source: ExtendedDamageSource,
            attackAttributes: List<CustomAttribute>,
            shieldBlocking: Boolean,
        ): DamageCalculationCommand {
            val damager = source.entity
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
            val damageType = source.type()

            val isProjectile = source.directEntity is Projectile
            val isProjectileCritical = (source.directEntity as? AbstractArrow)?.isCritArrow ?: false
            val playerCriticalAttack = (damager as? Player)?.wasLastAttackCritical() ?: false
            val isCritical = if (isProjectile) isProjectileCritical else playerCriticalAttack

            return DamageCalculationCommand(damager, damagerAttributes, damaged, damagedAttributes, damageType, world, isProjectile, shieldBlocking, isCritical)
        }
    }
}