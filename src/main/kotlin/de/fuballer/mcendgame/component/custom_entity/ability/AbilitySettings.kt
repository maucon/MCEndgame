package de.fuballer.mcendgame.component.custom_entity.ability

object AbilitySettings {
    const val ABILITY_CHECK_PERIOD = 5L // in ticks

    const val CHANGE_TARGET_CHECK_PERIOD = 10L // in ticks
    const val CHANGE_TARGET_MIN_COOLDOWN = 200L // in ticks
    const val CHANGE_TARGET_MAX_COOLDOWN = 400L // in ticks

    const val IDLE_CHECK_PERIOD = 10L
    const val MAX_IDLE_TIME = 400 // in ticks

    const val DEFAULT_TARGET_RANGE = 30.0

    fun getAbilityCooldown(mapTier: Int) = 30 + 120 / (1 + mapTier / 5)
}