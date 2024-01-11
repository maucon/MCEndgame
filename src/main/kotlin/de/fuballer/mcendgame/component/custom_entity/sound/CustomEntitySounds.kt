package de.fuballer.mcendgame.component.custom_entity.sound

import de.fuballer.mcendgame.component.custom_entity.data.CustomEntityType

object CustomEntitySounds {
    private val SOUNDS = mapOf(
        CustomEntityType.NAGA.toString() to CustomEntitySoundData("mcendgame_naga_hurt", "mcendgame_naga_death", "mcendgame_naga_hurt", ""),
        CustomEntityType.HARPY.toString() to CustomEntitySoundData("mcendgame_harpy_hurt", "mcendgame_harpy_death", "mcendgame_harpy_hurt", "")
    )

    fun getSounds(type: String) = SOUNDS[type]
}