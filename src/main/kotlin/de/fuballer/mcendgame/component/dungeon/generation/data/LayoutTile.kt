package de.fuballer.mcendgame.component.dungeon.generation.data

data class LayoutTile(
    var up: Boolean,
    var right: Boolean,
    var down: Boolean,
    var left: Boolean
) {
    fun getWaysAmount() = (if (up) 1 else 0) + (if (right) 1 else 0) + (if (down) 1 else 0) + (if (left) 1 else 0)

    fun getWays(): List<String> {
        val ways: MutableList<String> = mutableListOf()

        if (up) ways.add("up")
        if (right) ways.add("right")
        if (down) ways.add("down")
        if (left) ways.add("left")

        return ways
    }

    fun getBlockedWays(): List<String> {
        val ways: MutableList<String> = mutableListOf()

        if (!up) ways.add("up")
        if (!right) ways.add("right")
        if (!down) ways.add("down")
        if (!left) ways.add("left")

        return ways
    }

    fun getSchematicName(): String {
        return when (getWaysAmount()) {
            1 -> "1000"
            2 -> if (up == right || up == left) "0110" else "1010"
            3 -> "1110"
            4 -> "1111"
            else -> "0000"
        }
    }

    fun getRequiredWaysString() = (if (up) "1" else "0") + (if (right) "1" else "0") + (if (down) "1" else "0") + (if (left) "1" else "0")
}