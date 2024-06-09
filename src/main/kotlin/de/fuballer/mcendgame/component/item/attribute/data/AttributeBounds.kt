package de.fuballer.mcendgame.component.item.attribute.data

data class AttributeBounds(
    val min: Double,
    val max: Double
) {
    init {
        if (min > max) throw IllegalArgumentException("min cannot be greater than max")
    }
}