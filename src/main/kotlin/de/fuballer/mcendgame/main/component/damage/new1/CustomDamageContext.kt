package de.fuballer.mcendgame.main.component.damage.new1

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute

data class CustomDamageContext(
    val incomingDamage: IncomingDamage? = null,
    val extraAttackerAttributes: List<CustomAttribute> = listOf()
)