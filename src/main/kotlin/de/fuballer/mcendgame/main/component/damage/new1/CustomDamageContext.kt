package de.fuballer.mcendgame.main.component.damage.new1

import de.fuballer.mcendgame.main.component.custom_attribute.data.CustomAttribute

data class CustomDamageContext(
    var damageInstance: DamageInstance = DamageInstance(),
    val extraAttackerAttributes: List<CustomAttribute> = listOf()
)