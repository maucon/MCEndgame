package de.fuballer.mcendgame.client.component.render.link

import net.minecraft.world.phys.Vec3

data class LinkVertexData(
    val pos: Vec3,
    val color: Int,
    val light: Int,
    val thicknessFactor: Double,
)