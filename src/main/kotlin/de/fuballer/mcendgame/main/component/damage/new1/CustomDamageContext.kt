package de.fuballer.mcendgame.main.component.damage.new1

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute
import de.fuballer.mcendgame.main.component.damage.DamageCalculationCommand

data class CustomDamageContext(
    var damageInstance: DamageInstance? = null,
    val extraVictimAttributes: List<CustomAttribute> = listOf()
)