package de.fuballer.mcendgame.main.component.dungeon.generation.data

import de.fuballer.mcendgame.main.util.extension.Vec3iExtension.clone
import de.fuballer.mcendgame.main.util.extension.Vec3iExtension.rotateY90
import net.minecraft.core.Vec3i

data class Door(
    var pos: Vec3i,
    var dir: Vec3i,
) {
    fun getAdjacentPosition(): Vec3i = pos.clone().offset(dir)

    fun getRotated90(times: Int): Door {
        val newPosition = pos.rotateY90(times)
        val newDirection = dir.rotateY90(times)

        return Door(newPosition, newDirection)
    }

    fun getDirectionAsBlockRotation16(): Int {
        if (dir.z == 1) return 0
        if (dir.x == -1) return 4
        if (dir.z == -1) return 8
        if (dir.x == 1) return 12
        return 0
    }

    fun getOffset(vec: Vec3i) = Door(
        Vec3i(pos.x + vec.x, pos.y + vec.y, pos.z + vec.z),
        dir.clone()
    )
}