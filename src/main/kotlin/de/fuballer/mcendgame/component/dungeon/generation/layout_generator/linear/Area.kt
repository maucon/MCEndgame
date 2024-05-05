package de.fuballer.mcendgame.component.dungeon.generation.layout_generator.linear

import org.bukkit.util.Vector

data class Area(
    var pos1: Vector,
    var pos2: Vector
) {
    fun contains(
        pos: Vector
    ): Boolean {
        if (pos.x < pos1.x) return false
        if (pos.y < pos1.y) return false
        if (pos.z < pos1.z) return false
        if (pos.x > pos2.x) return false
        if (pos.y > pos2.y) return false
        if (pos.y > pos2.z) return false

        return true
    }
}