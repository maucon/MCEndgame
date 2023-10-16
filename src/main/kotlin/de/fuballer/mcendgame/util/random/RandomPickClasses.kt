package de.fuballer.mcendgame.util.random

open class RandomOption<T>(
    val weight: Int,
    val option: T,
)

class SortableRandomOption<T>(
    weight: Int,
    /** bigger is better, should be unique in set */
    val tier: Int,
    option: T,
) : RandomOption<T>(weight, option)
