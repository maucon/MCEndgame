package de.fuballer.mcendgame.main.component.entity.custom.attack.data

import de.fuballer.mcendgame.main.util.FindBlockPosUtil
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

data class SummonData(
    private val factory: (ServerLevel, LivingEntity, LivingEntity) -> Entity,
    private val getTargets: (ServerLevel, Entity, LivingEntity?) -> List<LivingEntity>,
    private val getCountPerTarget: (Int) -> Int,
    private val spawnPositionsSearchSteps: Int,
    private val maxSpawnDistanceToTarget: Double,
) {
    private val maxSpawnDistanceToTargetSqr = maxSpawnDistanceToTarget * maxSpawnDistanceToTarget

    fun apply(
        level: ServerLevel,
        summoner: LivingEntity,
        mainTarget: LivingEntity?,
    ) {
        val targets = getTargets(level, summoner, mainTarget)
        if (targets.isEmpty()) return
        val countPerTarget = getCountPerTarget(targets.size)
        if (countPerTarget <= 0) return

        val summonerBlockPos = BlockPos.containing(summoner.position())
        val possiblePositions = FindBlockPosUtil.findEmptyAboveSolid(level, summonerBlockPos, spawnPositionsSearchSteps)
        if (possiblePositions.isEmpty()) return

        targets.forEach { target ->
            val positionsInDistance = getPositionsWithinDistance(possiblePositions, target)
            val chosenPositions = positionsInDistance.shuffled().take(countPerTarget)

            chosenPositions.forEach { pos ->
                val summon = factory(level, summoner, target)
                summon.setPos(pos.x + 0.5, pos.y.toDouble(), pos.z + 0.5)
                level.addFreshEntity(summon)
            }
        }
    }

    private fun getPositionsWithinDistance(
        possiblePositions: List<BlockPos>,
        summon: Entity,
    ) = possiblePositions.filter { it.distToCenterSqr(summon.position()) <= maxSpawnDistanceToTargetSqr }
}