package de.fuballer.mcendgame.helper

enum class CommandAction(
    val actionName: String
) {
    GET("get"),
    SET("set");

    companion object {
        fun allNames() = entries.map { it.actionName }
    }
}
