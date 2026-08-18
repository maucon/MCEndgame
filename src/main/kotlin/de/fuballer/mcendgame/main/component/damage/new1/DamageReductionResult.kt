package de.fuballer.mcendgame.main.component.damage.new1

data class DamageReductionResult(
    val damage: Float,
    val resistedDamage: Float,
) {
    companion object {
        fun zero() = DamageReductionResult(0f, 0f)
    }
}

operator fun DamageReductionResult.plus(other: DamageReductionResult) =
    DamageReductionResult(
        damage = damage + other.damage,
        resistedDamage = resistedDamage + other.resistedDamage,
    )