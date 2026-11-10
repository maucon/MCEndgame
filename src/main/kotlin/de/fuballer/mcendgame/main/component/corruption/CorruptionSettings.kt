package de.fuballer.mcendgame.main.component.corruption

import de.fuballer.mcendgame.main.util.random.RandomOption
import kotlin.random.Random

object CorruptionSettings {
    const val ATTRIBUTE_CHANGE_MIN = 0.2
    const val ATTRIBUTE_CHANGE_MAX = 0.5
    fun getAttributeChange() = Random.nextDouble(ATTRIBUTE_CHANGE_MIN, ATTRIBUTE_CHANGE_MAX)

    val CORRUPTION_OUTCOMES = listOf(
        RandomOption(5, CorruptionOutcome.NOTHING),
        RandomOption(5, CorruptionOutcome.DESTROY),
        RandomOption(30, CorruptionOutcome.INCREASE_ENCHANT_LEVEL),
        RandomOption(15, CorruptionOutcome.LOWER_ENCHANT_LEVEL),
        RandomOption(10, CorruptionOutcome.ADD_ENCHANT),
        RandomOption(5, CorruptionOutcome.ADD_CURSE_ENCHANT),
        RandomOption(35, CorruptionOutcome.ENHANCE_ATTRIBUTE),
        RandomOption(25, CorruptionOutcome.DIMINISH_ATTRIBUTE),
    )
}