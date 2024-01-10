package de.fuballer.mcendgame.component.custom_entity.ability

object AbilitySettings {
    const val ABILITY_CHECK_PERIOD = 5L // in ticks
    const val MAX_IDLE_TIME = 400 // in ticks

    fun getAbilityCooldown(mapTier: Int) = 30 + 120 / (1 + mapTier / 5)
}