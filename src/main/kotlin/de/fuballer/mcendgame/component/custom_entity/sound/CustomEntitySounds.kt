package de.fuballer.mcendgame.component.custom_entity.sound

import de.fuballer.mcendgame.component.custom_entity.data.CustomEntityType

object CustomEntitySounds {
    private val SOUNDS = mapOf(
        CustomEntityType.NAGA.toString() to CustomEntitySoundData("mcendgame_naga_hurt", "mcendgame_naga_death", "", ""),
        CustomEntityType.HARPY.toString() to CustomEntitySoundData("mcendgame_harpy_hurt", "mcendgame_harpy_death", "", ""),
        CustomEntityType.DEMONIC_GOLEM.toString() to CustomEntitySoundData("mcendgame_demonic_golem_hurt", "mcendgame_demonic_golem_death", "", ""),
        CustomEntityType.CYCLOPS.toString() to CustomEntitySoundData("mcendgame_cyclops_hurt", "mcendgame_cyclops_death", "", ""),
        CustomEntityType.MINOTAUR.toString() to CustomEntitySoundData("mcendgame_minotaur_hurt", "mcendgame_minotaur_death", "", ""),
        CustomEntityType.MANDRAGORA.toString() to CustomEntitySoundData("mcendgame_mandragora_hurt", "mcendgame_mandragora_death", "", ""),
    )

    fun getSounds(type: String) = SOUNDS[type]
}