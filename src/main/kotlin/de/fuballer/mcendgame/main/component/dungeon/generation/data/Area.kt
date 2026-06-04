package de.fuballer.mcendgame.main.component.dungeon.generation.data

import net.minecraft.core.Vec3i

data class Area(
    var pos1: Vec3i, //pos1 < pos2
    var pos2: Vec3i
) {
    fun contains(
        pos: Vec3i
    ): Boolean {
        if (pos.x < pos1.x) return false
        if (pos.y < pos1.y) return false
        if (pos.z < pos1.z) return false
        if (pos.x > pos2.x) return false
        if (pos.y > pos2.y) return false
        if (pos.z > pos2.z) return false

        return true
    }
}