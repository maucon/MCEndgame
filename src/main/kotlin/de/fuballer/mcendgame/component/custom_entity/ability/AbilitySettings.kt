package de.fuballer.mcendgame.component.custom_entity.ability

object AbilitySettings {
    const val ABILITY_CHECK_PERIOD = 5L // in ticks
    const val MAX_IDLE_TIME = 400 // in ticks

    const val INACTIVE_CHECK_PERIOD = 60 * 20L // in ticks

    fun getAbilityCooldown(mapTier: Int) = 30 + 120 / (1 + mapTier / 5)
}