package de.fuballer.mcendgame.component.dungeon.modifier

data class Modifier(
    val type: ModifierType,
    val operation: ModifierOperation,
    val value: Double
)