package de.fuballer.mcendgame.main.component.damage.new1

import com.mojang.logging.LogUtils
import de.fuballer.mcendgame.main.component.custom_attribute.effects.dodge.DodgeSettings
import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.damage.calculator.BaseDamageCalculator
import de.fuballer.mcendgame.main.component.damage.dodge.DodgeCalculationCommand
import de.fuballer.mcendgame.main.component.damage.ignore_damage.IgnoreDamageCommand
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDodgedEvent
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.getLastHurt
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.getLastResisted
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.isInInvulnerabilityFrames
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.setLastHitWasApplied
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.setLastHurt
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.setLastResisted
import de.maucon.mauconframework.command.CommandGateway
import de.maucon.mauconframework.event.EventGateway
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.stats.Stats
import net.minecraft.tags.DamageTypeTags
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity
import java.util.*
import kotlin.math.max
import kotlin.math.roundToInt

// TODO   if (damageCalculationConfig.isArmadilloDamageReduction) {
//            combinedDamage = (combinedDamage - 1f) / 2f
//        }
//        if (damageCalculationConfig.isEnderDragonDamageReduction) {
//            combinedDamage = combinedDamage / 4f + min(combinedDamage, 1.0f)
//        }
//        return damageCalculationConfig.difficultyScaling.scaleDamage(combinedDamage)

// TODO player, enderdragon, armadillo mixin

// TODO damageTypeKeys for
//  ignore damage dealt scaling (increase, more, decreased, less damage)
//  ignore damage taken scaling (increase, more, decreased, less damage taken)
//  ignore dodge or dodgeable

private val DAMAGE_CALCULATORS = listOf(
//    CreeperExplosionCalculator,
//    PufferfishTouchCalculator,
//    PierceAttackDamageCalculator,
//    KineticAttackDamageCalculator,
//    SpellDamageCalculator,
//    EnderDragonCalculator,
//    WitherSkullCalculator,
//    WitherExplosionCalculator,
//    SonicBoomCalculator,
//    GuardianMagicCalculator,
//    GuardianThornsCalculator,
//    TridentProjectileCalculator,
//    SmallFireballCalculator,
//    FireballCalculator,
//    AbstractArrowCalculator,
//    SnowballCalculator,
//    WindChargeCalculator,
//    ThornsCalculator,
//    MagicDamageCalculator,
//    PotionCalculator,
//    MaceSmashAttackCalculator,
//    GenericAttackCalculator,
//    MeleeAttackCalculator,
//    ShulkerBulletCalculator,
//    OtherProjectilesCalculator, // do not move
    // TODO add calculators one after another
    BaseDamageCalculator // do not move
)

object DamageService {
    private val log = LogUtils.getLogger()

    fun createDamageSourceResult(
        victim: LivingEntity,
        serverLevel: ServerLevel,
        damageSource: DamageSourceDraft,
        damage: Float, // for debug
    ): DamageSourceResult {
        log.info("createDamageSourceResult - ${victim.javaClass.simpleName} - ${damageSource.javaClass.simpleName} - originalDamage: $damage")

        val vanillaDamageContext = damageSource.vanillaDamageContext
        val customDamageContext = damageSource.customDamageContext

        val damageCalculationCommand = DamageCalculationCommand.of(
            victim,
            serverLevel,
            damageSource,
            customDamageContext.extraVictimAttributes, // FIXME this is attacker not victim - add victim
            vanillaDamageContext.isBlocked()
        )
        damageCalculationCommand.moreDamageTaken.addAll(vanillaDamageContext.getVictimMoreDamageTaken())

        val cmd = CommandGateway.apply(damageCalculationCommand)

        // TODO could also be empty cmd -- maybe move before DamageCalculationCommand
        if (isDamageIgnored(victim, damageSource)) {
            return DamageSourceResult.NoDamage(damageSource)
        }
        if (isDamageDodged(victim, damageSource)) {
            return DamageSourceResult.NoDamage(damageSource)
        }

        if (cmd.isShieldBlocking) {
            return DamageSourceResult.ZeroDamage(cmd, damageSource)
        }

        // TODO special damage reduction
        //      armadillo, ender-dragon, difficulty

        // TODO calculate damage with calculators
        // TODO update damageCalculators
        val damageCalculator = DAMAGE_CALCULATORS.firstOrNull { it.isActive(damageSource) }!!
        // TODO calculate final Damage Instance
        val attackDamage = damageCalculator.calculateDamage(damage, victim, damageSource, cmd)
        // TODO fix this holy
        val damageInstance = DamageInstance().setAttackDamage(attackDamage)

        // TODO damage type changing cmd (10% of AD to True Damage)

        return DamageSourceResult.Applied(damageInstance, cmd, damageSource)
    }

    /**
     * Has side effects, only use in damage-related mixins.
     */
    fun applyDamage(
        victim: LivingEntity,
        source: DamageSource,
        damage: Float // for debug
    ): Optional<Float> {
        log.info("applyDamage - ${victim.javaClass.simpleName} - ${source.javaClass.simpleName} - damage: $damage")
        var dmg = damage

        val damageReductionResult = calculateIncomingDamage(victim, source, dmg)
        val reducedTotal = damageReductionResult.damage
        val resistedTotal = damageReductionResult.resistedDamage

        victim.setLastHitWasApplied(true)

        var damageToApply = reducedTotal
        var damageResisted = resistedTotal
        if (victim.isInInvulnerabilityFrames()) {
            val isStronger = reducedTotal > victim.getLastHurt()
            victim.setLastHitWasApplied(isStronger)

            if (!isStronger) return Optional.empty();

            damageToApply = reducedTotal - victim.getLastHurt()
            damageResisted = max(0f, resistedTotal - victim.getLastResisted())
        }
        victim.setLastHurt(reducedTotal)
        victim.setLastResisted(resistedTotal)

        dmg = damageToApply

        // from LivingEntity.getDamageAfterArmorAbsorb
        if (!source.`is`(DamageTypeTags.BYPASSES_ARMOR)) {
            victim.hurtArmor(source, dmg) // TODO only ATTACK_DAMAGE part?
        }

        // from LivingEntity.getDamageAfterMagicAbsorb
        if (victim is ServerPlayer) {
            victim.awardStat(Stats.DAMAGE_RESISTED, (damageResisted * 10.0f).roundToInt())
        } else if (source.entity is ServerPlayer) {
            (source.entity as ServerPlayer).awardStat(Stats.DAMAGE_DEALT_RESISTED, (damageResisted * 10.0f).roundToInt())
        }

        return Optional.of(dmg)
    }

    private fun isDamageIgnored(
        entity: LivingEntity,
        source: DamageSource
    ): Boolean {
        val cmd = IgnoreDamageCommand.of(entity, source)
            .let { CommandGateway.apply(it) }

        return cmd.ignoreDamage
    }

    private fun isDamageDodged(
        entity: LivingEntity,
        source: DamageSource
    ): Boolean {
        val key = source.typeHolder().unwrapKey()
        if (key.isEmpty) return false

        // TODO should be DamageTypeKey
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

    private fun calculateIncomingDamage(
        victim: LivingEntity,
        source: DamageSource,
        damage: Float // for debug
    ): DamageReductionResult {
        log.info("calculateIncomingDamage - ${victim.javaClass.simpleName} - ${source.javaClass.simpleName} - damage: $damage")
        if (source !is DamageSourceResult.Applied) {
            // todo log/debug
            return DamageReductionResult.zero()
        }
        log.info(source.damageInstance.toString())

        // TODO event? command? LivingEntityDamagedEvent?

        return source.damageInstance.getAfterDamageReduction(victim, source, source.damageCalculationCommand)
    }
}