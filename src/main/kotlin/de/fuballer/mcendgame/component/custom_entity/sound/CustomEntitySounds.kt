package de.fuballer.mcendgame.component.custom_entity.sound

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import de.fuballer.mcendgame.component.custom_entity.types.harpy.HarpyEntityType
import de.fuballer.mcendgame.component.custom_entity.types.naga.NagaEntityType

object CustomEntitySounds {
    private val SOUNDS = mapOf(
        NagaEntityType to CustomEntitySoundData("mcendgame_naga_hurt", "mcendgame_naga_death", "mcendgame_naga_hurt"),
        HarpyEntityType to CustomEntitySoundData("mcendgame_harpy_hurt", "mcendgame_harpy_death", "mcendgame_harpy_hurt")
    )

    fun getSounds(type: CustomEntityType) = SOUNDS[type]
}