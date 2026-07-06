package de.fuballer.mcendgame.main.component.damage.dealing

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.custom_type.CustomDamageTypes
import net.minecraft.entity.Entity
import net.minecraft.entity.damage.DamageType
import net.minecraft.registry.RegistryKey
import net.minecraft.server.world.ServerWorld

object DamageDealingExtension {
    fun Entity.dealElementalSpellDamage(damagePercentage: Double, attacker: Entity) {
        val attributes = listOf(
            CustomAttribute(CustomAttributeTypes.MORE_DAMAGE, rolls = listOf(DoubleRoll(DoubleBounds(damagePercentage - 1)))),
            CustomAttribute(CustomAttributeTypes.NO_ATTACK_DAMAGE),
        )

        dealDamage(attacker, attributes, CustomDamageTypes.SPELL)
    }

    fun Entity.dealGenericAttackDamage(amount: Float, attacker: Entity, blockable: Boolean = true) {
        val serverWorld = entityWorld as? ServerWorld ?: return

        val damageType = if (blockable) CustomDamageTypes.GENERIC_ATTACK else CustomDamageTypes.GENERIC_ATTACK_UNBLOCKABLE
        val damageSource = CustomDamageTypes.of(serverWorld, damageType, attacker)
        val extended = ExtendedDamageSource(DamageCalculationConfig(), damageSource)

        this.damage(extended, amount)
    }

    fun Entity.dealDamage(
        attacker: Entity,
        attributes: List<CustomAttribute>,
        damageType: RegistryKey<DamageType>,
    ) {
        val serverWorld = entityWorld as? ServerWorld ?: return

        val damageSource = CustomDamageTypes.of(serverWorld, damageType, attacker)
        val config = DamageCalculationConfig(attackAttributes = attributes)
        val extended = ExtendedDamageSource(config, damageSource)

        this.damage(extended, 420F) // amount does not matter, will be calculated by us
    }
}