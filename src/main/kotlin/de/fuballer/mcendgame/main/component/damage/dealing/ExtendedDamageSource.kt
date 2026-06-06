package de.fuballer.mcendgame.main.component.damage.dealing

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.damage.DifficultyScaling
import net.minecraft.core.Holder
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.Entity

class ExtendedDamageSource(
    val damageCalculationConfig: DamageCalculationConfig,
    type: Holder<DamageType>,
    source: Entity?,
    attacker: Entity?
) : DamageSource(type, source, attacker) {
    constructor(damageCalculationConfig: DamageCalculationConfig, damageSource: DamageSource)
            : this(damageCalculationConfig, damageSource.typeHolder(), damageSource.directEntity, damageSource.entity)

    constructor(damageSource: DamageSource)
            : this(DamageCalculationConfig(), damageSource)
}

data class DamageCalculationConfig(
    var isArmadilloDamageReduction: Boolean = false,
    var isEnderDragonDamageReduction: Boolean = false,
    var difficultyScaling: DifficultyScaling = DifficultyScaling.NONE,

    val vanillaMoreDamage: MutableList<Double> = mutableListOf(),
    val vanillaMoreDamageTaken: MutableList<Double> = mutableListOf(),
    var shieldBlocked: Boolean = false,
    val attackAttributes: List<CustomAttribute> = mutableListOf(),
) {
    constructor() : this(false) // explicitly empty constructor for java

    fun armadilloDamageReduction(armadilloDamageReduction: Boolean): DamageCalculationConfig {
        this.isArmadilloDamageReduction = armadilloDamageReduction
        return this
    }

    fun enderDragonDamageReduction(enderDragonDamageReduction: Boolean): DamageCalculationConfig {
        this.isEnderDragonDamageReduction = enderDragonDamageReduction
        return this
    }

    fun difficultyScaling(difficultyScaling: DifficultyScaling): DamageCalculationConfig {
        this.difficultyScaling = difficultyScaling
        return this
    }
}
