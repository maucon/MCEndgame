package de.fuballer.mcendgame.domain

data class EntitySoundData(
    val hurt: String,
    val death: String
) {
    companion object {
        fun create(name: String) = EntitySoundData("mcendgame_${name}_hurt", "mcendgame_${name}_death")
    }
}