package de.fuballer.mcendgame.main.component.entity.custom.attack.trigger_condition

import de.fuballer.mcendgame.main.util.BlockPosUtil
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.phys.AABB

class TargetMaxDistanceToGroundTriggerCondition(
    private val maxDistance: Double,
) : TriggerCondition() {
    override fun doesTrigger(attacker: Mob, target: LivingEntity?): Boolean {
        if (target == null) return false

        val box = target.boundingBox
        val groundY = findGroundY(target, box) ?: return false

        return box.minY - groundY <= maxDistance
    }

    private fun findGroundY(
        entity: Entity,
        box: AABB,
    ): Double? {
        val level = entity.level() as? ServerLevel ?: return null

        var highestY: Double? = null

        val minX = Mth.floor(box.minX)
        val maxX = Mth.floor(box.maxX)
        val minZ = Mth.floor(box.minZ)
        val maxZ = Mth.floor(box.maxZ)

        val minY = Mth.floor(box.minY - maxDistance - 1.0)
        val maxY = Mth.floor(box.minY)

        for (x in minX..maxX) {
            for (z in minZ..maxZ) {
                for (y in minY..maxY) {
                    val pos = BlockPos(x, y, z)
                    val surfaceY = BlockPosUtil.getHighestY(level, pos) ?: continue
                    if (surfaceY <= box.minY + 0.001 && (highestY == null || surfaceY > highestY)) highestY = surfaceY
                }
            }
        }

        return highestY
    }
}