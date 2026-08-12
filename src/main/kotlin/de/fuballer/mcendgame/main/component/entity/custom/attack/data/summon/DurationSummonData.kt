package de.fuballer.mcendgame.main.component.entity.custom.attack.data.summon

import de.fuballer.mcendgame.main.util.BlockPosUtil
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import kotlin.random.Random

data class DurationSummonData(
    private val factory: (ServerLevel, LivingEntity) -> Entity,
    private val getTargets: (ServerLevel, LivingEntity, LivingEntity?) -> List<LivingEntity>,
    private val getCount: (Int) -> Int,
    private val spawnPositionsSearchSteps: Int,
    private val minDistanceBetweenSummons: Double = 1.0,
    private val validSpawnPosition: (ServerLevel, BlockPos) -> Boolean = { _, _ -> true },
) {
    private val minDistanceBetweenSummonsSquared = minDistanceBetweenSummons * minDistanceBetweenSummons

    fun summon(
        level: ServerLevel,
        summoner: LivingEntity,
        pos: BlockPos,
    ) {
        val summon = factory(level, summoner)
        summon.setPos(pos.x + 0.5, pos.y.toDouble(), pos.z + 0.5)
        level.addFreshEntity(summon)
    }

    fun getSpawnPositionsWithDelay(
        level: ServerLevel,
        summoner: LivingEntity,
        target: LivingEntity?,
        durationStart: Int,
        durationEnd: Int = durationStart,
    ): Map<BlockPos, Int> {
        val summonerBlockPos = BlockPos.containing(summoner.position())
        val possiblePositions = BlockPosUtil.findEmptyAboveSolid(level, summonerBlockPos, spawnPositionsSearchSteps)
            .filter { validSpawnPosition(level, it) }

        val targets = getTargets(level, summoner, target).size
        val summonCount = getCount(targets)
        val chosenPositions = choosePositions(possiblePositions, summonCount)

        val positionsWithDelay = chosenPositions.associateWith { Random.nextInt(durationStart, durationEnd) }
        return positionsWithDelay
    }

    private fun choosePositions(
        possiblePositions: List<BlockPos>,
        count: Int,
    ): List<BlockPos> {
        val shuffled = possiblePositions.shuffled()
        val chosen = mutableListOf<BlockPos>()

        for (pos in shuffled) {
            if (!chosen.all { it.distSqr(pos) >= minDistanceBetweenSummonsSquared }) continue

            chosen += pos
            if (chosen.size == count) break
        }

        return chosen
    }
}