package de.fuballer.mcendgame.util.command

enum class CommandAction(
    val actionName: String
) {
    GET("get"),
    SET("set");

    companion object {
        fun allNames() = entries.map { it.actionName }
    }
}
