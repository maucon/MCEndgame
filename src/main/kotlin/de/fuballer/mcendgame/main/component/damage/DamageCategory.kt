package de.fuballer.mcendgame.main.component.damage

import com.mojang.logging.LogUtils
import de.fuballer.mcendgame.main.component.damage.calculator.DamageCalculator
import de.fuballer.mcendgame.main.component.damage.calculator.attack_damage.AbstractArrowCalculator
import de.fuballer.mcendgame.main.component.damage.calculator.attack_damage.BaseAttackDamageCalculator
import de.fuballer.mcendgame.main.component.damage.calculator.attack_damage.GenericAttackCalculator
import de.fuballer.mcendgame.main.component.damage.calculator.attack_damage.KineticAttackDamageCalculator
import de.fuballer.mcendgame.main.component.damage.calculator.attack_damage.MaceSmashAttackCalculator
import de.fuballer.mcendgame.main.component.damage.calculator.attack_damage.MeleeAttackCalculator
import de.fuballer.mcendgame.main.component.damage.calculator.attack_damage.PierceAttackDamageCalculator
import de.fuballer.mcendgame.main.component.damage.calculator.attack_damage.PotionCalculator
import de.fuballer.mcendgame.main.component.damage.calculator.attack_damage.TridentProjectileCalculator
import de.fuballer.mcendgame.main.component.damage.calculator.spell_damage.BaseSpellDamageCalculator
import de.fuballer.mcendgame.main.component.damage.calculator.spell_damage.SpellDamageCalculator
import de.fuballer.mcendgame.main.component.damage.calculator.true_damage.TrueDamageCalculator
import de.fuballer.mcendgame.main.component.damage.reducer.AttackDamageReducer
import de.fuballer.mcendgame.main.component.damage.reducer.DamageReducer
import de.fuballer.mcendgame.main.component.damage.reducer.SpellDamageReducer
import de.fuballer.mcendgame.main.component.damage.reducer.TrueDamageReducer
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity

enum class DamageCategory(
    private val reducer: DamageReducer,
    private val calculators: List<DamageCalculator>,
) {
    ATTACK_DAMAGE(
        AttackDamageReducer,
        listOf(
            // CustomDamageTypes
            GenericAttackCalculator,
            PierceAttackDamageCalculator,
            KineticAttackDamageCalculator,
            // Calculating damage custom
            TridentProjectileCalculator,
            MaceSmashAttackCalculator,
            PotionCalculator,
            AbstractArrowCalculator,
            MeleeAttackCalculator,
            BaseAttackDamageCalculator
        )
    ),
    SPELL_DAMAGE(
        SpellDamageReducer,
        listOf(
            SpellDamageCalculator,
            BaseSpellDamageCalculator
        )
    ),
    TRUE_DAMAGE(
        TrueDamageReducer,
        listOf(TrueDamageCalculator)
    );

    private val log = LogUtils.getLogger()

    fun calculate(amount: Float, victim: LivingEntity, source: DamageSource, cmd: DamageCalculationCommand): Float =
        calculators.firstOrNull { it.isActive(source) }
            ?.also { log.debug("damageCalculator: ${it.javaClass.simpleName}") }
            ?.calculateDamage(amount, victim, source, cmd)
            ?: throw IllegalStateException("No active DamageCalculator for category=$this, source=$source")

    fun applyDamageReduction(damage: Float, attacked: LivingEntity, source: DamageSource, cmd: DamageCalculationCommand) =
        reducer.apply(damage, attacked, source, cmd)
}