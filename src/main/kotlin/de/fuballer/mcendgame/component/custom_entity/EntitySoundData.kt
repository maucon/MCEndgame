package de.fuballer.mcendgame.component.custom_entity

data class EntitySoundData(
    val hurt: String,
    val death: String
) {
    companion object {
        fun create(name: String) = EntitySoundData("mcendgame_${name}_hurt", "mcendgame_${name}_death")
    }
}