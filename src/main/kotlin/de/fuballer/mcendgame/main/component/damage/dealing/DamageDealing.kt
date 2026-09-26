package de.fuballer.mcendgame.main.component.damage.dealing

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleBounds
import de.fuballer.mcendgame.main.component.custom_attribute.data.DoubleRoll
import de.fuballer.mcendgame.main.component.custom_attribute.types.CustomAttributeTypes
import de.fuballer.mcendgame.main.component.damage.custom_type.CustomDamageTypes
import de.fuballer.mcendgame.main.component.damage.data.CustomDamageContext
import de.fuballer.mcendgame.main.component.damage.DamageCategory
import de.fuballer.mcendgame.main.component.damage.data.DamageSourceDraft
import de.fuballer.mcendgame.main.component.damage.data.IncomingDamage
import de.fuballer.mcendgame.main.component.damage.data.VanillaDamageContext
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.Entity

/**
 * Utility functions for dealing damage to entities through the custom damage system.
 */
object DamageDealing {
    /**
     * Deals generic attack damage to this entity.
     *
     * @param damageAmount the amount of damage to deal
     * @param causingEntity the entity causing the damage (e.g. skeleton)
     * @param blockable whether the damage can be blocked (e.g. with a shield)
     */
    fun Entity.dealGenericAttackDamage(
        damageAmount: Float,
        causingEntity: Entity,
        blockable: Boolean = true
    ) = dealDamage(
        damageType = if (blockable) CustomDamageTypes.GENERIC_ATTACK else CustomDamageTypes.GENERIC_ATTACK_UNBLOCKABLE,
        DamageCategory.ATTACK_DAMAGE,
        damageAmount,
        causingEntity
    )

    /**
     * Deals spell damage to this entity. The actual damage amount is calculated from the
     * causing entity's spell damage stat, modified by [flatDamage] and [damageMultiplier].
     *
     * @param causingEntity the entity causing the damage (e.g. a skeleton mage)
     * @param directEntity the entity dealing the damage (e.g. a spell projectile)
     * @param damageMultiplier multiplier applied to the total spell damage (1.0 = no change)
     * @param flatDamage flat spell damage bonus added before multipliers
     */
    fun Entity.dealSpellDamage(
        causingEntity: Entity,
        directEntity: Entity? = causingEntity,
        damageMultiplier: Double = 1.0,
        flatDamage: Double = 0.0,
    ) = dealDamage(
        CustomDamageTypes.SPELL,
        DamageCategory.SPELL_DAMAGE,
        flatDamage.toFloat(), // mock value, spell damage will be calculated by entities spell damage
        causingEntity,
        directEntity,
        extraAttributes = listOf(
            CustomAttribute(CustomAttributeTypes.SPELL_DAMAGE, roll = DoubleRoll(DoubleBounds(flatDamage))),
            CustomAttribute(CustomAttributeTypes.MORE_DAMAGE, roll = DoubleRoll(DoubleBounds(damageMultiplier - 1)))
        )
    )

    /**
     * Deals damage to this entity using the custom damage system.
     *
     * @param damageType the kind of attack
     * @param damageCategory the category of the damage
     * @param damageAmount the amount of damage
     * @param causingEntity the entity causing the damage (e.g. skeleton)
     * @param directEntity the entity dealing the damage (e.g. arrow)
     * @param extraAttributes additional custom attributes to apply to the damage
     * @return whether the damage was successfully applied
     */
    fun Entity.dealDamage(
        damageType: ResourceKey<DamageType>,
        damageCategory: DamageCategory,
        damageAmount: Float,
        causingEntity: Entity?,
        directEntity: Entity? = causingEntity,
        extraAttributes: List<CustomAttribute> = emptyList(),
    ): Boolean {
        val serverWorld = level() as? ServerLevel ?: return false

        val damageSource = CustomDamageTypes.of(serverWorld, damageType, causingEntity, directEntity)

        val incomingDamage = IncomingDamage(damageCategory, damageAmount)
        val customDamageContext = CustomDamageContext(incomingDamage, extraAttributes)

        val damageSourceDraft = DamageSourceDraft(VanillaDamageContext(), customDamageContext, damageSource)
        return this.hurtServer(serverWorld, damageSourceDraft, damageAmount)
    }
}