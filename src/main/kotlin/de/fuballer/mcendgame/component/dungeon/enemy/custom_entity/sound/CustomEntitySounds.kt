package de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.sound

import de.fuballer.mcendgame.component.dungeon.enemy.custom_entity.data.CustomEntityType

object CustomEntitySounds {
    private val SOUNDS = mapOf(
        Pair(CustomEntityType.NAGA.toString(), CustomEntitySoundData("mcendgame_naga_hurt", "mcendgame_naga_death", "mcendgame_naga_hurt"))
    )

    fun getSounds(type: String) = SOUNDS[type]
}