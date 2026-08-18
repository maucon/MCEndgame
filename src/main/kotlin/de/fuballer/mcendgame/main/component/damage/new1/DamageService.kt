package de.fuballer.mcendgame.main.component.damage.new1

import com.mojang.logging.LogUtils
import de.fuballer.mcendgame.main.component.custom_attribute.effects.dodge.DodgeSettings
import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand
import de.fuballer.mcendgame.main.component.damage.calculator.BaseDamageCalculator
import de.fuballer.mcendgame.main.component.damage.dodge.DodgeCalculationCommand
import de.fuballer.mcendgame.main.component.damage.ignore_damage.IgnoreDamageCommand
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDodgedEvent
import de.maucon.mauconframework.command.CommandGateway
import de.maucon.mauconframework.event.EventGateway
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity

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
        damage: Float,
    ): DamageSourceResult {
        log.info("createDamageSourceResult - ${victim.javaClass.simpleName} - ${damageSource.javaClass.simpleName}")

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

    fun calculateIncomingDamage(
        victim: LivingEntity,
        source: DamageSource,
        damage: Float
    ): DamageReductionResult {
        log.info("calculateReducedDamage - ${victim.javaClass.simpleName} - ${source.javaClass.simpleName}")
        if (source !is DamageSourceResult.Applied) {
            // todo log/debug
            return DamageReductionResult.zero()
        }
        log.info(source.damageInstance.toString())

        // TODO event? command? LivingEntityDamagedEvent?

        return source.damageInstance.getAfterDamageReduction(victim, source, source.damageCalculationCommand)
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
}