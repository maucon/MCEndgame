package de.fuballer.mcendgame.main.component.entity.custom.attack.data.summon

import de.fuballer.mcendgame.main.component.entity.custom.attack.data.DelayedAttackData
import de.fuballer.mcendgame.main.component.entity.custom.attack.data.DelayedAttackDataInstance
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity

class DelayedDurationSummonData(
    val durationStart: Int,
    val durationEnd: Int = durationStart,
    private val summonData: DurationSummonData,
    delay: Int = 0,
) : DelayedAttackData(delay) {
    override fun getInstance(target: LivingEntity?): DelayedAttackDataInstance = DelayedDurationSummonDataInstance(this)

    fun summon(
        level: ServerLevel,
        summoner: LivingEntity,
        pos: BlockPos,
    ) {
        summonData.summon(level, summoner, pos)
    }

    fun getSpawnPositionsWithDelay(
        level: ServerLevel,
        summoner: LivingEntity,
        target: LivingEntity?,
    ) = summonData.getSpawnPositionsWithDelay(level, summoner, target, durationStart, durationEnd)
}