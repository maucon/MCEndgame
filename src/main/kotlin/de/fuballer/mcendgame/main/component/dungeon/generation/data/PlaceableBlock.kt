package de.fuballer.mcendgame.main.component.dungeon.generation.data

import de.fuballer.mcendgame.main.util.extension.Vec3iExtension.rotateY90
import de.fuballer.mcendgame.main.util.extension.Vec3iExtension.toBlockPos
import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.world.level.block.Block

data class PlaceableBlock(
    val x: Int,
    val y: Int,
    val z: Int,
    val rotation16: Int,
    val block: Block,
) {
    fun getBlockPos(
        offset: Vec3i = Vec3i(0, 0, 0),
        rotation90: Int = 0,
    ): BlockPos {
        val rotatedPos = Vec3i(x, y, z).rotateY90(rotation90)
        val summedPos = rotatedPos.offset(offset)
        return summedPos.toBlockPos()
    }
}