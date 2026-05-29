package de.fuballer.mcendgame.main.component.dungeon.generation.room_types.loader

import de.fuballer.mcendgame.main.component.dungeon.generation.data.Door
import de.fuballer.mcendgame.main.component.dungeon.generation.data.RoomMarkerPoints
import de.fuballer.mcendgame.main.component.dungeon.generation.data.SpawnPosition
import net.minecraft.util.math.Vec3i

object MirrorUtil {
    /** Mirror a single block position (flip X). */
    fun mirrorPos(pos: Vec3i, size: Vec3i): Vec3i =
        Vec3i(size.x - 1 - pos.x, pos.y, pos.z)

    /** Mirror a direction vector (negate X component). */
    fun mirrorDir(dir: Vec3i): Vec3i =
        Vec3i(-dir.x, dir.y, dir.z)

    /** Mirror a yaw rotation (negate). */
    fun mirrorRot(rot: Double): Double = -rot

    fun mirrorSpawnPosition(sp: SpawnPosition, size: Vec3i) = SpawnPosition(
        pos = mirrorPos(sp.pos, size),
        rot = mirrorRot(sp.rot)
    )

    fun mirrorDoor(door: Door, size: Vec3i) = Door(
        pos = mirrorPos(door.pos, size),
        dir = mirrorDir(door.dir)
    )

    fun mirrorMarkerPoints(points: RoomMarkerPoints, size: Vec3i) = RoomMarkerPoints(
        startPos = points.startPos?.let { mirrorPos(it, size) },
        monsterPos = points.monsterPos.map { mirrorSpawnPosition(it, size) }.toMutableList(),
        bossPos = points.bossPos.map { mirrorSpawnPosition(it, size) }.toMutableList(),
        doors = points.doors.map { mirrorDoor(it, size) }.toMutableList(),
        encounterPos = points.encounterPos.map { mirrorPos(it, size) }.toMutableList(),
        startEncounterPos = points.startEncounterPos.map { mirrorPos(it, size) }.toMutableList(),
    )
}