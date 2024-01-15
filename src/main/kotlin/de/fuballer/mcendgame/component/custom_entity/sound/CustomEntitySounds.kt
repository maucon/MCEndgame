package de.fuballer.mcendgame.component.custom_entity.sound

import de.fuballer.mcendgame.domain.entity.CustomEntityType
import de.fuballer.mcendgame.domain.entity.harpy.HarpyEntityType
import de.fuballer.mcendgame.domain.entity.naga.NagaEntityType

private const val PREFIX = "mcendgame"

object CustomEntitySounds {
    private val SOUNDS = mapOf(
        NagaEntityType to CustomEntitySoundData("${PREFIX}_naga_hurt", "${PREFIX}_naga_death", "${PREFIX}_naga_hurt"),
        HarpyEntityType to CustomEntitySoundData("${PREFIX}_harpy_hurt", "${PREFIX}_harpy_death", "${PREFIX}_harpy_hurt")
    )

    fun getSounds(type: CustomEntityType) = SOUNDS[type]
}