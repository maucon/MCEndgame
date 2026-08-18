package de.fuballer.mcendgame.main.component.entity.custom.attack.data.summon

import de.fuballer.mcendgame.main.component.entity.custom.attack.data.DelayedAttackDataInstance
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

class DelayedDurationSummonDataInstance(
    private val durationSummonData: DelayedDurationSummonData,
    attackSpeed: Double,
) : DelayedAttackDataInstance(durationSummonData, attackSpeed) {
    private val durationStart = (durationSummonData.durationStart / attackSpeed).toInt()
    private val durationEnd = (durationSummonData.durationEnd / attackSpeed).toInt()
    var summonLocations: Map<BlockPos, Int>? = null

    override fun tick(
        level: ServerLevel,
        entity: Mob,
        target: LivingEntity?,
    ): Boolean {
        if (durationSummonData.shouldCancel(entity)) return true

        if (summonLocations == null) summonLocations = durationSummonData.getSpawnPositionsWithDelay(level, entity, target, durationStart, durationEnd)

        age++
        if (age < durationStart) return false
        if (age > durationEnd) return true

        summonLocations!!
            .filter { (_, delay) -> delay == age }
            .forEach { (pos, _) -> durationSummonData.summon(level, entity, pos) }
        return false
    }
}