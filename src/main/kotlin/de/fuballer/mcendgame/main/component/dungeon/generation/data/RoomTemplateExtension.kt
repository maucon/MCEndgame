package de.fuballer.mcendgame.main.component.dungeon.generation.data

import net.minecraft.core.Vec3i
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate

data class RoomTemplateExtension(
    val template: StructureTemplate,
    val offset: Vec3i,
)