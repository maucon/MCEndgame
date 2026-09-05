package de.fuballer.mcendgame.main.component.damage.new1

import de.fuballer.mcendgame.main.component.damage.DifficultyScaling

class VanillaDamageContext {
    private val victimMoreDamageTaken = mutableListOf<Double>()
    private var blocked = false
    private var difficultyScaling = DifficultyScaling.NONE
    private var customDamageReduction: (Float) -> Float = { it } // e.g. armadillo or ender dragon

    fun setBlocked(blocked: Boolean) {
        this.blocked = blocked
    }

    fun setDifficultyScaling(difficultyScaling: DifficultyScaling) {
        this.difficultyScaling = difficultyScaling
    }

    fun addVictimMoreDamageTaken(roll: Double) {
        victimMoreDamageTaken.add(roll)
    }

    fun setCustomDamageReduction(customDamageReduction: (Float) -> Float) {
        this.customDamageReduction = customDamageReduction
    }

    fun isBlocked() = blocked
    fun getVictimMoreDamageTaken() = victimMoreDamageTaken.toList()
    fun getDifficultyScaling() = difficultyScaling
    fun getCustomDamageReduction() = customDamageReduction
}