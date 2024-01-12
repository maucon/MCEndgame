package de.fuballer.mcendgame.component.custom_entity.sound

import de.fuballer.mcendgame.component.custom_entity.types.CustomEntityType
import de.fuballer.mcendgame.component.custom_entity.types.cyclops.CyclopsEntityType
import de.fuballer.mcendgame.component.custom_entity.types.demoic_golem.DemonicGolemEntityType
import de.fuballer.mcendgame.component.custom_entity.types.harpy.HarpyEntityType
import de.fuballer.mcendgame.component.custom_entity.types.mandragora.MandragoraEntityType
import de.fuballer.mcendgame.component.custom_entity.types.minotaur.MinotaurEntityType
import de.fuballer.mcendgame.component.custom_entity.types.naga.NagaEntityType

object CustomEntitySounds {
    private val SOUNDS = mapOf(
        NagaEntityType to CustomEntitySoundData("mcendgame_naga_hurt", "mcendgame_naga_death"),
        HarpyEntityType to CustomEntitySoundData("mcendgame_harpy_hurt", "mcendgame_harpy_death"),
        DemonicGolemEntityType to CustomEntitySoundData("mcendgame_demonic_golem_hurt", "mcendgame_demonic_golem_death"),
        CyclopsEntityType to CustomEntitySoundData("mcendgame_cyclops_hurt", "mcendgame_cyclops_death"),
        MinotaurEntityType to CustomEntitySoundData("mcendgame_minotaur_hurt", "mcendgame_minotaur_death"),
        MandragoraEntityType to CustomEntitySoundData("mcendgame_mandragora_hurt", "mcendgame_mandragora_death"),
    )

    fun getSounds(type: CustomEntityType) = SOUNDS[type]
}