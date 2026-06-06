package de.fuballer.mcendgame.main.component.dungeon.generation.data

import net.minecraft.core.Vec3i
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate

data class RoomType(
    val path: String, // used for debugging
    val template: StructureTemplate,
    val markerPoints: RoomMarkerPoints,
    val size: Vec3i = template.size,
    val extensions: List<RoomTemplateExtension> = listOf(), // only extend in positive dimensions
    val mirrored: Boolean = false
) {
    fun isLinear() = markerPoints.doors.size == 2
    fun getComplexity() = markerPoints.monsterPos.size + 1
}