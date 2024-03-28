package de.fuballer.mcendgame.component.dungeon.generation.data

import org.bukkit.util.Vector

data class RoomType(
    var name: String,
    var size: Vector,// 8 blocks (inclusive) -> 7 size
    var complexity: Int,
    var doors: List<Door>
) {
    fun getRotated(rad: Double): RoomType {
        val rotatedDoors = mutableListOf<Door>()
        for (door in doors) rotatedDoors.add(door.getRotated(rad))

        return RoomType(name, size.clone().rotateAroundY(rad), complexity, rotatedDoors)
    }
}