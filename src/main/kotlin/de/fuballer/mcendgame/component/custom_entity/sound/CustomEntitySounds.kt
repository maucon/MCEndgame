package de.fuballer.mcendgame.component.custom_entity.sound

import de.fuballer.mcendgame.component.custom_entity.CustomEntityType

object CustomEntitySounds {
    private val SOUNDS = mapOf(
        CustomEntityType.NAGA to CustomEntitySoundData("mcendgame_naga_hurt", "mcendgame_naga_death", "mcendgame_naga_hurt"),
        CustomEntityType.HARPY to CustomEntitySoundData("mcendgame_harpy_hurt", "mcendgame_harpy_death", "mcendgame_harpy_hurt")
    )

    fun getSounds(type: CustomEntityType) = SOUNDS[type]
}