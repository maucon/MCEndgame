package de.fuballer.mcendgame.main.component.damage

import com.mojang.logging.LogUtils
import de.fuballer.mcendgame.main.component.damage.data.*
import de.fuballer.mcendgame.main.component.damage.dodge.DodgeCalculationCommand
import de.fuballer.mcendgame.main.component.damage.ignore_damage.IgnoreDamageCommand
import de.fuballer.mcendgame.main.component.tags.CustomTags
import de.fuballer.mcendgame.main.messaging.misc.LivingEntityDamagedEvent
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

// TODO check for knockback. is it applied once or double? check sulfur cube / check custom knockback attacks
// TODO AttackKnockbackUtil
object DamageService {
    private val log = LogUtils.getLogger()

    fun createDamageSourceResult(
        victim: LivingEntity,
        serverLevel: ServerLevel,
        damageSource: DamageSourceDraft,
        damage: Float,
    ): DamageSourceResult {
        if (isDamageIgnored(victim, damageSource)) {
            return DamageSourceResult.NoDamage(damageSource)
        }

        val vanillaDamageContext = damageSource.vanillaDamageContext
        val customDamageContext = damageSource.customDamageContext

        val damageCalculationCommand = DamageCalculationCommand.of(
            victim,
            serverLevel,
            damageSource,
            customDamageContext.extraAttackerAttributes,
            vanillaDamageContext.isBlocked()
        )
        damageCalculationCommand.moreDamageTaken.addAll(vanillaDamageContext.getVictimMoreDamageTaken())

        val cmd = CommandGateway.apply(damageCalculationCommand)

        if (cmd.isShieldBlocking) {
            return DamageSourceResult.ZeroDamage(damageSource)
        }

        if (isDamageDodged(victim, damageSource)) {
            return DamageSourceResult.NoDamage(damageSource)
        }

        val incomingDamage = customDamageContext.incomingDamage
            ?: IncomingDamage(DamageCategory.ATTACK_DAMAGE, damage) // vanilla is attack damage only

        val calculated = incomingDamage.category.calculate(incomingDamage.amount, victim, damageSource, cmd)
        val categorizedDamage = CategorizedDamage(incomingDamage.category to calculated)

        return DamageSourceResult.Applied(categorizedDamage, cmd, vanillaDamageContext, damageSource)
    }

    /**
     * Has side effects, only use in damage-related mixins.
     */
    fun applyDamage(
        victim: LivingEntity,
        source: DamageSource
    ): Optional<Float> {
        val (reducedTotal, resistedTotal) = calculateIncomingDamage(victim, source)
        val damageTotal = reducedTotal.coerceAtLeast(0f)

        victim.setLastHitWasApplied(true)

        var damageToApply = damageTotal
        var damageResisted = resistedTotal
        if (victim.isInInvulnerabilityFrames()) {
            val isStronger = damageTotal > victim.getLastHurt()
            victim.setLastHitWasApplied(isStronger)

            if (!isStronger) return Optional.empty()

            damageToApply = damageTotal - victim.getLastHurt()
            damageResisted = max(0f, resistedTotal - victim.getLastResisted())
        }
        victim.setLastHurt(damageTotal)
        victim.setLastResisted(resistedTotal)

        val dmg = damageToApply

        // from LivingEntity.getDamageAfterArmorAbsorb
        if (!source.`is`(DamageTypeTags.BYPASSES_ARMOR)) {
            victim.hurtArmor(source, dmg) // FIXME: maybe only attack/spell damage part?
        }

        // from LivingEntity.getDamageAfterMagicAbsorb
        if (victim is ServerPlayer) {
            victim.awardStat(Stats.DAMAGE_RESISTED, (damageResisted * 10.0f).roundToInt())
        } else if (source.entity is ServerPlayer) {
            (source.entity as ServerPlayer).awardStat(Stats.DAMAGE_DEALT_RESISTED, (damageResisted * 10.0f).roundToInt())
        }

        val event = LivingEntityDamagedEvent(victim, source, dmg)
        EventGateway.publish(event)

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
        // Damage types in BYPASS_DODGE (environmental damage such as fire, fall, drown, ...) can
        // never be dodged. Everything else - attacks and projectiles in particular - can.
        if (source.`is`(CustomTags.BYPASS_DODGE)) return false;

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
    ): DamageReductionResult {
        // only DamageSourceResult.Applied does apply damage to the victim otherwise return zero
        if (source !is DamageSourceResult.Applied) {
            if (source !is DamageSourceResult) {
                log.error("Expected DamageSourceResult but got ${source.javaClass.simpleName} - dealing no damage")
            }
            return DamageReductionResult.zero()
        }

        // FIXME should spell/true damage scale with difficulty?
        val scaledDamageInstance = source.categorizedDamage.transformDamage { _, damage ->
            val reduced = source.vanillaDamageContext.getCustomDamageReduction().invoke(damage)
            source.vanillaDamageContext.getDifficultyScaling().scaleDamage(reduced)
        }

        val cmd = source.damageCalculationCommand
        val afterDamageReduction = scaledDamageInstance.getAfterDamageReduction(victim, source, cmd)

        val flatDamageTaken = cmd.flatDamageTaken.sum().toFloat()
        return afterDamageReduction + DamageReductionResult(flatDamageTaken, 0f)
    }
}