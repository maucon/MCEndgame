package de.fuballer.mcendgame.component.damage.calculators

import de.fuballer.mcendgame.component.damage.DamageCalculationEvent
import de.fuballer.mcendgame.util.DamageUtil
import de.fuballer.mcendgame.util.EntityUtil
import de.fuballer.mcendgame.util.extension.WorldExtension.isDungeonWorld
import org.bukkit.Difficulty
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent.DamageCause
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier

abstract class DamageCauseCalculator {
    /**
     * The damage cause the calculator is referring to.
     */
    abstract val damageType: DamageCause

    /**
     * Whether the damage can be blocked with a shield.
     */
    abstract val canBeBlocked: Boolean

    /**
     * Whether the damage is affected/reduced by the invulnerability frames.
     */
    abstract val affectedByInvulnerability: Boolean

    /**
     * Whether the damage is affected/reduced by the entity's armor and armor toughness.
     */
    abstract val affectedByArmor: Boolean

    /**
     * Whether the damage dealt by non-player entities is scaled with the difficulty.
     */
    abstract val scaledByDifficulty: Boolean

    /**
     * Whether the damage is affected/reduced by the entity's armor protection enchant.
     */
    abstract val affectedByArmorProtection: Boolean

    /**
     * Optional damage reducing enchantment. (default null)
     */
    open val specialEnchantDamageReduction: Enchantment? = null

    /**
     * Whether the damage is affected/reduced by the entity's resistance potion effect. (default true)
     */
    open val affectedByResistance = true

    /**
     * Whether the damage is affected/reduced by the entity's absorption potion effect. (default true)
     */
    open val affectedByAbsorption = true

    /**
     * Builds a DamageCalculationEvent based on the EntityDamageByEntityEvent.
     *
     * @param event The EntityDamageByEntityEvent.
     * @return A DamageCalculationEvent representing the calculated damage, or null if the event cannot be processed.
     */
    open fun buildDamageEvent(event: EntityDamageByEntityEvent): DamageCalculationEvent? {
        val damageEvent = buildBaseDamageEvent(event) ?: return null

        if (damageEvent.damager is Player) return buildDamageEventForPlayer(event, damageEvent)
        return buildDamageEventForNonPlayer(event, damageEvent)
    }

    /**
     * Builds a DamageCalculationEvent specifically for player entities.
     *
     * @param event        The EntityDamageByEntityEvent.
     * @param damageEvent  The base DamageCalculationEvent.
     * @return A modified DamageCalculationEvent for player entities.
     */
    open fun buildDamageEventForPlayer(event: EntityDamageByEntityEvent, damageEvent: DamageCalculationEvent): DamageCalculationEvent {
        damageEvent.baseDamage.add(event.damage)
        return damageEvent
    }

    /**
     * Builds a DamageCalculationEvent specifically for non-player entities.
     *
     * @param event        The EntityDamageByEntityEvent.
     * @param damageEvent  The base DamageCalculationEvent.
     * @return A modified DamageCalculationEvent for non-player entities.
     */
    open fun buildDamageEventForNonPlayer(event: EntityDamageByEntityEvent, damageEvent: DamageCalculationEvent): DamageCalculationEvent {
        val rawDamage =
            if (scaledByDifficulty) DamageUtil.reverseDifficultyDamage(damageEvent.difficulty, event.damage)
            else event.damage

        damageEvent.baseDamage.add(rawDamage)
        return damageEvent
    }

    /**
     * Calculates the base damage for the given DamageCalculationEvent.
     *
     * @param event The DamageCalculationEvent.
     * @return The base damage value.
     */
    open fun getBaseDamage(event: DamageCalculationEvent) =
        if (event.damager is Player) getBaseDamageForPlayer(event)
        else getBaseDamageForNonPlayer(event)

    /**
     * Calculates the base damage for player entities.
     *
     * @param event The DamageCalculationEvent.
     * @return The base damage value for player entities.
     */
    open fun getBaseDamageForPlayer(event: DamageCalculationEvent): Double {
        val normalBaseDamage = getNormalBaseDamage(event)
        return scaleInvulnerabilityDamage(event.damaged, normalBaseDamage)
    }

    /**
     * Calculates the base damage for non-player entities.
     *
     * @param event The DamageCalculationEvent.
     * @return The base damage value for non-player entities.
     */
    open fun getBaseDamageForNonPlayer(event: DamageCalculationEvent): Double {
        val normalBaseDamage = getNormalBaseDamage(event)
        val baseDamage =
            if (event.isDungeonWorld) normalBaseDamage // do not scale damage in dungeon worlds
            else scaleDifficultyDamage(event.difficulty, normalBaseDamage)

        return scaleInvulnerabilityDamage(event.damaged, baseDamage)
    }

    /**
     * Retrieves the normal base damage for the given DamageCalculationEvent.
     * Normal means that the damage is not scaled by difficulty or other values (invulnerability frames)
     *
     * @param event The DamageCalculationEvent.
     * @return The normal base damage value.
     */
    open fun getNormalBaseDamage(event: DamageCalculationEvent) = DamageUtil.getRawBaseDamage(event)

    /**
     * Scales the damage based on the game difficulty.
     *
     * @param difficulty The game difficulty.
     * @param damage     The damage value to be scaled.
     * @return The scaled damage value.
     */
    open fun scaleDifficultyDamage(difficulty: Difficulty, damage: Double) =
        if (scaledByDifficulty) DamageUtil.scaleDifficultyDamage(difficulty, damage) else damage

    /**
     * Scales the damage based on invulnerability of the entity.
     *
     * @param entity The LivingEntity receiving the damage.
     * @param damage The damage value to be scaled.
     * @return The scaled damage value.
     */
    open fun scaleInvulnerabilityDamage(entity: LivingEntity, damage: Double) =
        if (affectedByInvulnerability) DamageUtil.scaleInvulnerabilityDamage(entity, damage) else damage

    /**
     * Retrieves the flat damage reduction for the given DamageCalculationEvent and damage modifier.
     *
     * @param event    The DamageCalculationEvent.
     * @param damage   The damage value.
     * @param modifier The damage modifier.
     * @return The flat damage reduction value.
     * @throws IllegalArgumentException if the specified modifier is not applicable for damage reduction.
     */
    fun getFlatDamageReduction(event: DamageCalculationEvent, damage: Double, modifier: DamageModifier) =
        when (modifier) {
            DamageModifier.BASE -> throw IllegalArgumentException("$modifier is not a damage reduction")
            DamageModifier.HARD_HAT -> throw IllegalArgumentException("$modifier is not valid") // should not happen
            DamageModifier.BLOCKING -> getFlatBlockingDamageReduction(event, damage)
            DamageModifier.ARMOR -> getFlatArmorDamageReduction(event, damage)
            DamageModifier.RESISTANCE -> getFlatResistanceDamageReduction(event, damage)
            DamageModifier.MAGIC -> getFlatMagicDamageReduction(event, damage)
            DamageModifier.ABSORPTION -> getFlatAbsorptionDamageReduction(event, damage)
        }

    /**
     * Calculates the flat damage reduction based on whether the damage is blocked or not.
     *
     * @param event  The DamageCalculationEvent.
     * @param damage The damage value before reduction.
     * @return The reduced damage value based on whether the damage is blocked or not.
     */
    open fun getFlatBlockingDamageReduction(event: DamageCalculationEvent, damage: Double) =
        if (canBeBlocked && event.isDamageBlocked) damage else 0.0

    /**
     * Calculates the flat damage reduction based on armor.
     *
     * @param event  The DamageCalculationEvent.
     * @param damage The damage value before reduction.
     * @return The reduced damage value based on armor.
     */
    open fun getFlatArmorDamageReduction(event: DamageCalculationEvent, damage: Double): Double {
        val reduction = if (affectedByArmor) DamageUtil.getArmorDamageReduction(event.damaged, damage) else 0.0
        return damage * reduction
    }

    /**
     * Calculates the flat damage reduction based on resistance effects.
     *
     * @param event  The DamageCalculationEvent.
     * @param damage The damage value before reduction.
     * @return The reduced damage value based on resistance effects.
     */
    open fun getFlatResistanceDamageReduction(event: DamageCalculationEvent, damage: Double): Double {
        val reduction = if (affectedByResistance) DamageUtil.getResistancePotionEffectDamageReduction(event.damaged) else 0.0
        return damage * reduction
    }

    /**
     * Calculates the flat damage reduction based on magic (armor enchants and potion resistance).
     *
     * @param event  The DamageCalculationEvent.
     * @param damage The damage value before reduction.
     * @return The reduced damage value based on magic.
     */
    open fun getFlatMagicDamageReduction(event: DamageCalculationEvent, damage: Double): Double {
        if (!affectedByArmorProtection) return 0.0

        val reduction = specialEnchantDamageReduction?.let { DamageUtil.getSpecialEnchantDamageReduction(event.damaged, it) }
            ?: DamageUtil.getProtectionDamageReduction(event.damaged)

        return damage * reduction
    }

    /**
     * Calculates the flat damage reduction based on absorption effects.
     *
     * @param event  The DamageCalculationEvent.
     * @param damage The damage value before reduction.
     * @return The reduced damage value based on absorption effects.
     */
    open fun getFlatAbsorptionDamageReduction(event: DamageCalculationEvent, damage: Double) =
        if (affectedByAbsorption) DamageUtil.getAbsorbedDamage(event.damaged, damage) else 0.0

    /**
     * Builds a DamageCalculationEvent based on the provided EntityDamageByEntityEvent.
     *
     * @param event The EntityDamageByEntityEvent to build the DamageCalculationEvent from.
     * @return A DamageCalculationEvent representing the damage calculation for the given event,
     *         or null if the event cannot be processed.
     */
    private fun buildBaseDamageEvent(event: EntityDamageByEntityEvent): DamageCalculationEvent? {
        val damager = EntityUtil.getLivingEntityDamager(event.damager) ?: return null
        val damagedEntity = event.entity as? LivingEntity ?: return null
        val cause = event.cause
        val isDungeonWorld = event.damager.world.isDungeonWorld()
        val damageBlocked = event.getDamage(DamageModifier.BLOCKING) < 0
        val difficulty = damager.world.difficulty

        return DamageCalculationEvent(event, this, damager, damagedEntity, cause, isDungeonWorld, damageBlocked, difficulty)
    }
}