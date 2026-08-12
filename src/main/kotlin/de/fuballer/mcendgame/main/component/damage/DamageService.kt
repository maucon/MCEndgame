package de.fuballer.mcendgame.main.component.damage

import com.mojang.logging.LogUtils
import de.fuballer.mcendgame.main.component.custom_attribute.effects.SpellResistanceSettings
import de.fuballer.mcendgame.main.component.custom_attribute.effects.dodge.DodgeSettings
import de.fuballer.mcendgame.main.component.damage.calculator.*
import de.fuballer.mcendgame.main.component.damage.dealing.DamageCalculationConfig
import de.fuballer.mcendgame.main.component.damage.dealing.ExtendedDamageSource
import de.fuballer.mcendgame.main.component.damage.dodge.DodgeCalculationCommand
import de.fuballer.mcendgame.main.component.damage.ignore_damage.IgnoreDamageCommand
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDodgedEvent
import de.maucon.mauconframework.command.CommandGateway
import de.maucon.mauconframework.event.EventGateway
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.stats.Stats
import net.minecraft.tags.DamageTypeTags
import net.minecraft.world.damagesource.CombatRules
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.enchantment.EnchantmentHelper
import kotlin.math.min
import kotlin.math.roundToInt

private val DAMAGE_CALCULATORS = listOf(
    CreeperExplosionCalculator,
    PufferfishTouchCalculator,
    PierceAttackDamageCalculator,
    KineticAttackDamageCalculator,
    SpellDamageCalculator,
    EnderDragonCalculator,
    WitherSkullCalculator,
    WitherExplosionCalculator,
    SonicBoomCalculator,
    GuardianMagicCalculator,
    GuardianThornsCalculator,
    TridentProjectileCalculator,
    SmallFireballCalculator,
    FireballCalculator,
    AbstractArrowCalculator,
    SnowballCalculator,
    WindChargeCalculator,
    ThornsCalculator,
    MagicDamageCalculator,
    PotionCalculator,
    MaceSmashAttackCalculator,
    MeleeAttackCalculator,
    ShulkerBulletCalculator,
    OtherProjectilesCalculator, // do not move
    BaseDamageCalculator // do not move
)

object DamageService {
    private val log = LogUtils.getLogger()

    fun calculateFinalDamage(
        entity: LivingEntity,
        world: ServerLevel,
        source: ExtendedDamageSource,
        originalDamage: Float
    ): DamageCalculationResult {
        val damageCalculationConfig = source.damageCalculationConfig

        if (isDamageIgnored(entity, source)) {
            return DamageCalculationResult.noDamage()
        }
        if (isDamageDodged(entity, source)) {
            return DamageCalculationResult.noDamage()
        }

        val damageCalculationCommand = DamageCalculationCommand.of(entity, world, source, damageCalculationConfig.attackAttributes, damageCalculationConfig.shieldBlocked)
        damageCalculationCommand.moreDamage.addAll(damageCalculationConfig.vanillaMoreDamage)
        damageCalculationCommand.moreDamageTaken.addAll(damageCalculationConfig.vanillaMoreDamageTaken)

        val cmd = CommandGateway.apply(damageCalculationCommand)

        if (cmd.isShieldBlocking) {
            return DamageCalculationResult.normalDamage(0f)
        }

        val finalAmount = calculateFinalDamage(originalDamage, entity, source, damageCalculationConfig, cmd)
        return DamageCalculationResult.normalDamage(finalAmount)
    }

    private fun isDamageIgnored(
        entity: LivingEntity,
        source: ExtendedDamageSource
    ): Boolean {
        val cmd = IgnoreDamageCommand.of(entity, source)
            .let { CommandGateway.apply(it) }

        return cmd.ignoreDamage
    }

    private fun isDamageDodged(
        entity: LivingEntity,
        source: ExtendedDamageSource
    ): Boolean {
        val key = source.typeHolder().unwrapKey()
        if (key.isEmpty) return false
        if (DodgeSettings.BYPASS_DODGE.contains(key.get())) return false

        val dodgeCalculationCommand = DodgeCalculationCommand.of(entity, source)
            .let { CommandGateway.apply(it) }

        if (dodgeCalculationCommand.isDodging()) {
            val dodgeEvent = LivingEntityDodgedEvent(entity, source.directEntity, source.entity)
            EventGateway.publish(dodgeEvent)
            return true
        }

        return false
    }

    /** calculates the final damage dealt to the hit pool of the target */
    fun calculateFinalDamage(
        originalDamage: Float,
        attacked: LivingEntity,
        source: ExtendedDamageSource,
        damageCalculationConfig: DamageCalculationConfig,
        cmd: DamageCalculationCommand
    ): Float {
        val damageCalculator = DAMAGE_CALCULATORS.firstOrNull { it.isActive(source) }!!

        var attackDamage = damageCalculator.calculateAttackDamage(originalDamage, attacked, source, cmd)
        var spellDamage = damageCalculator.calculateSpellDamage(originalDamage, attacked, source, cmd)

        log.debug("${attacked.javaClass.simpleName} [${damageCalculator.javaClass.simpleName}]: originalDamage: $originalDamage --> calculated damage: ${attackDamage + spellDamage} ($attackDamage + $spellDamage)")

        attackDamage = calculateAttackDamageReduction(attackDamage, attacked, source, cmd)
        spellDamage = calculateSpellDamageReduction(spellDamage, attacked, source, cmd)

        var combinedDamage = attackDamage + spellDamage

        // Special damage calculation
        if (damageCalculationConfig.isArmadilloDamageReduction) {
            combinedDamage = (combinedDamage - 1f) / 2f
        }
        if (damageCalculationConfig.isEnderDragonDamageReduction) {
            combinedDamage = combinedDamage / 4f + min(combinedDamage, 1.0f)
        }
        return damageCalculationConfig.difficultyScaling.scaleDamage(combinedDamage)
    }

    private fun calculateAttackDamageReduction(
        damage: Float,
        attacked: LivingEntity,
        source: DamageSource,
        cmd: DamageCalculationCommand
    ): Float {
        var attackDamage = applyArmorToDamage(damage, source, attacked)
        attackDamage = modifyAppliedDamage(source, attackDamage, attacked)
        return DamageUtil.applyDamageTakenAttributes(attackDamage, cmd)
    }

    private fun calculateSpellDamageReduction(
        damage: Float,
        attacked: LivingEntity,
        source: DamageSource,
        cmd: DamageCalculationCommand
    ): Float {
        var spellDamage = applySpellResistanceToDamage(damage, cmd)
        spellDamage = modifyAppliedDamage(source, spellDamage, attacked)
        return DamageUtil.applyDamageTakenAttributes(spellDamage, cmd)
    }

    private fun applyArmorToDamage(
        amount: Float,
        source: DamageSource,
        entity: LivingEntity
    ): Float {
        var amount = amount
        if (source.`is`(DamageTypeTags.BYPASSES_ARMOR)) return amount

        val armorToughness = entity.getAttributeValue(Attributes.ARMOR_TOUGHNESS).toFloat()
        amount = DamageUtil.reduceAttackDamageByArmor(entity, amount, source, entity.armorValue.toFloat(), armorToughness)

        return amount
    }

    private fun applySpellResistanceToDamage(
        amount: Float,
        cmd: DamageCalculationCommand,
    ): Float {
        var amount = amount
        val spellResistance = min(SpellResistanceSettings.LIMIT, cmd.spellResistance.sum()).toFloat()
        amount *= 1 - spellResistance

        return amount
    }

    private fun modifyAppliedDamage(
        source: DamageSource,
        amount: Float,
        entity: LivingEntity
    ): Float {
        var amount = amount

        if (!source.`is`(DamageTypeTags.BYPASSES_EFFECTS)) {
            if (entity.hasEffect(MobEffects.RESISTANCE) && !source.`is`(DamageTypeTags.BYPASSES_RESISTANCE)) {
                val resistance = entity.getEffect(MobEffects.RESISTANCE)!!.amplifier + 1

                val resistancePercent = resistance * 0.2f
                val resistedDamage = min(amount * resistancePercent, amount)
                amount -= resistedDamage

                if (resistedDamage > 0.0f && resistedDamage < 3.4028235E37f) {
                    if (entity is ServerPlayer) {
                        entity.awardStat(Stats.DAMAGE_RESISTED, (resistedDamage * 10.0f).roundToInt())
                    } else if (source.entity is ServerPlayer) {
                        (source.entity as ServerPlayer).awardStat(Stats.DAMAGE_DEALT_RESISTED, (resistedDamage * 10.0f).roundToInt())
                    }
                }
            }
        }

        if (amount <= 0.0f) return 0.0f
        if (source.`is`(DamageTypeTags.BYPASSES_ENCHANTMENTS)) return amount
        val serverWorld = entity.level() as? ServerLevel ?: return amount

        val protectionAmount = EnchantmentHelper.getDamageProtection(serverWorld, entity, source)
        amount = CombatRules.getDamageAfterMagicAbsorb(amount, protectionAmount)

        return amount
    }
}