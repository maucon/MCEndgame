package de.fuballer.mcendgame.main.component.entity.custom.attack.data.summon

import de.fuballer.mcendgame.main.util.BlockPosUtil
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.phys.Vec3

class SummonAtTargetData(
    factory: (ServerLevel, LivingEntity, LivingEntity) -> Entity,
    private val searchForGroundDistance: Boolean = false,
) : SummonData(factory) {
    override fun apply(
        level: ServerLevel,
        summoner: LivingEntity,
        target: LivingEntity?,
    ) {
        if (target == null) return

        val summon = factory(level, summoner, target)

        if (!searchForGroundDistance) {
            summon.setPos(target.x, target.y, target.z)
            level.addFreshEntity(summon)
            return
        }

        val box = target.boundingBox

        val positions = listOf(
            Vec3(target.x, target.y, target.z),
            Vec3(box.minX, target.y, box.minZ),
            Vec3(box.minX, target.y, box.maxZ),
            Vec3(box.maxX, target.y, box.minZ),
            Vec3(box.maxX, target.y, box.maxZ),
        )

        val groundPosition = positions.mapNotNull { pos -> findGroundPosition(level, pos) }
            .minByOrNull { (pos, groundY) -> pos.y - groundY }

        if (groundPosition != null) {
            val (pos, groundY) = groundPosition
            summon.setPos(pos.x, groundY, pos.z)
        } else summon.setPos(target.x, target.y, target.z)

        level.addFreshEntity(summon)
    }

    private fun findGroundPosition(
        level: ServerLevel,
        position: Vec3,
    ): Pair<Vec3, Double>? {
        val startY = Mth.floor(position.y)
        val minY = level.minY

        for (y in startY downTo minY) {
            val blockPos = BlockPos(Mth.floor(position.x), y, Mth.floor(position.z))
            val groundY = BlockPosUtil.getHighestY(level, blockPos) ?: continue
            if (groundY <= position.y) return position to groundY
        }

        return null
    }
}