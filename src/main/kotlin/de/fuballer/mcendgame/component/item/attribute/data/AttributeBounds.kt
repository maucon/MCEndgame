package de.fuballer.mcendgame.component.item.attribute.data

data class AttributeBounds(
    val min: Double,
    val max: Double
) {
    init {
        assert(min <= max) { "min must be smaller than or equal max" }
    }
}