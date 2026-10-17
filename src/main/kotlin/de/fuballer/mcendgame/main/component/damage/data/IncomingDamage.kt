package de.fuballer.mcendgame.main.component.damage.data

import de.fuballer.mcendgame.main.component.damage.DamageCategory

data class IncomingDamage(
    val category: DamageCategory,
    val amount: Float
)