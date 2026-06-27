package de.fuballer.mcendgame.main.component.config


data class UserConfig(
    val enableAnalytics: Boolean = true
) {
    companion object {
        const val FILE = "mcendgame.json"
    }
}