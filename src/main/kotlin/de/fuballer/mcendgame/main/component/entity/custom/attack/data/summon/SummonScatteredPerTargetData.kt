package de.fuballer.mcendgame.main.component.entity.custom.attack.data.summon

import de.fuballer.mcendgame.main.util.BlockPosUtil
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

class SummonScatteredPerTargetData(
    factory: (ServerLevel, LivingEntity, LivingEntity) -> Entity,
    private val getTargets: (ServerLevel, LivingEntity, LivingEntity?) -> List<LivingEntity>,
    private val getCountPerTarget: (Int) -> Int,
    private val spawnPositionsSearchSteps: Int,
    maxSpawnDistanceToTarget: Double,
) : SummonData(factory) {
    private val maxSpawnDistanceToTargetSqr = maxSpawnDistanceToTarget * maxSpawnDistanceToTarget

    override fun apply(
        level: ServerLevel,
        summoner: LivingEntity,
        target: LivingEntity?,
    ) {
        val targets = getTargets(level, summoner, target)
        if (targets.isEmpty()) return
        val countPerTarget = getCountPerTarget(targets.size)
        if (countPerTarget <= 0) return

        val summonerBlockPos = BlockPos.containing(summoner.position())
        val possiblePositions = BlockPosUtil.findEmptyAboveSolid(level, summonerBlockPos, spawnPositionsSearchSteps)
        if (possiblePositions.isEmpty()) return

        targets.forEach {
            val positionsInDistance = getPositionsWithinDistance(possiblePositions, it)
            val chosenPositions = positionsInDistance.shuffled().take(countPerTarget)

            chosenPositions.forEach { pos ->
                val summon = factory(level, summoner, it)
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