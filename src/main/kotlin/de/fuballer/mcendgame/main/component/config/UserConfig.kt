package de.fuballer.mcendgame.main.component.config


data class UserConfig(
    val sendAnalytics: Boolean = true
) {
    companion object {
        const val FILE = "mcendgame.json"
    }
}