package de.fuballer.mcendgame.main.util.extension

import net.minecraft.core.BlockPos
import net.minecraft.world.phys.Vec3

object BlockPosExtension {
    fun BlockPos.toVec3d(): Vec3 {
        return Vec3(x.toDouble(), y.toDouble(), z.toDouble())
    }
}