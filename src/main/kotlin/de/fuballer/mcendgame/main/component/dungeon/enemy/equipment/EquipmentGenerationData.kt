package de.fuballer.mcendgame.main.component.dungeon.enemy.equipment

import net.minecraft.item.trim.ArmorTrim

data class EquipmentGenerationData(
    val uniqueProbability: Double,
    val luckyAttributes: Boolean,
    val additionalAttributeProbabilities: List<Double>,
    val armorTrim: ArmorTrim?,
)
