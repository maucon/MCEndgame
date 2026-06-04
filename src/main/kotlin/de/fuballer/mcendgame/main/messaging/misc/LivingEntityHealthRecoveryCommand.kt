package de.fuballer.mcendgame.main.messaging.misc

import net.minecraft.world.entity.LivingEntity

data class LivingEntityHealthRecoveryCommand(
    val entity: LivingEntity,
    val originalAmount: Float,
    val increase: MutableList<Float> = mutableListOf(),
    val more: MutableList<Float> = mutableListOf(),
) {
    constructor(entity: LivingEntity, originalAmount: Float) : this(entity, originalAmount, mutableListOf(), mutableListOf())

    fun getFinalAmount(): Float {
        var amount = originalAmount
        amount *= 1 + increase.sum()
        amount *= more.fold(1F) { a, b -> a * (b + 1) }
        return amount
    }
}