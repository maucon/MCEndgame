package de.fuballer.mcendgame.main.component.damage.dealing

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.custom_type.CustomDamageTypes
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.Entity

object DamageDealingExtension {
    fun Entity.dealSpellDamage(
        damagePercentage: Double,
        causingEntity: Entity,
        directEntity: Entity? = causingEntity,
    ): Boolean {
        val attributes = listOf(
            CustomAttribute(CustomAttributeTypes.MORE_DAMAGE, rolls = listOf(DoubleRoll(DoubleBounds(damagePercentage - 1)))),
            CustomAttribute(CustomAttributeTypes.NO_ATTACK_DAMAGE),
        )

        return dealDamage(attributes, CustomDamageTypes.SPELL, causingEntity, directEntity)
    }

    fun Entity.dealGenericAttackDamage(amount: Float, attacker: Entity, blockable: Boolean = true): Boolean {
        val serverWorld = level() as? ServerLevel ?: return false

        val damageType = if (blockable) CustomDamageTypes.GENERIC_ATTACK else CustomDamageTypes.GENERIC_ATTACK_UNBLOCKABLE
        val damageSource = CustomDamageTypes.of(serverWorld, damageType, attacker)
        val extended = ExtendedDamageSource(DamageCalculationConfig(), damageSource)

        return this.hurtServer(serverWorld, extended, amount)
    }

    fun Entity.dealDamage(
        attributes: List<CustomAttribute>,
        damageType: ResourceKey<DamageType>,
        causingEntity: Entity,
        directEntity: Entity? = causingEntity,
    ): Boolean {
        val serverWorld = level() as? ServerLevel ?: return false

        val damageSource = CustomDamageTypes.of(serverWorld, damageType, causingEntity, directEntity)
        val config = DamageCalculationConfig(attackAttributes = attributes)
        val extended = ExtendedDamageSource(config, damageSource)

        return this.hurtServer(serverWorld, extended, 420F) // amount does not matter, will be calculated by us
    }
}