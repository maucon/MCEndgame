package de.fuballer.mcendgame.main.component.dungeon.generation.data

import net.minecraft.core.Vec3i

data class RoomMarkerPoints(
    var startPos: Vec3i?,
    val monsterPos: MutableList<SpawnPosition>,
    val bossPos: MutableList<SpawnPosition>,
    val doors: MutableList<Door>,
    val encounterPos: MutableList<Vec3i>,
    val startEncounterPos: MutableList<Vec3i>,
) {
    companion object {
        fun fromImmutable(
            startPos: Vec3i?,
            monsterPos: List<SpawnPosition>,
            bossPos: List<SpawnPosition>,
            doors: List<Door>,
            encounterPos: List<Vec3i>,
            startEncounterPos: List<Vec3i>,
        ): RoomMarkerPoints {
            return RoomMarkerPoints(
                startPos,
                monsterPos.toMutableList(),
                bossPos.toMutableList(),
                doors.toMutableList(),
                encounterPos.toMutableList(),
                startEncounterPos.toMutableList(),
            )
        }
    }

    fun add(markerPoints: RoomMarkerPoints) {
        if (startPos == null) {
            startPos = markerPoints.startPos
        }

        monsterPos.addAll(markerPoints.monsterPos)
        bossPos.addAll(markerPoints.bossPos)
        doors.addAll(markerPoints.doors)
        encounterPos.addAll(markerPoints.encounterPos)
        startEncounterPos.addAll(markerPoints.startEncounterPos)
    }
}
