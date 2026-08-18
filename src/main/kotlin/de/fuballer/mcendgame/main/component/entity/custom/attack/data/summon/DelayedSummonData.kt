package de.fuballer.mcendgame.main.component.entity.custom.attack.data.summon

import de.fuballer.mcendgame.main.component.entity.custom.attack.data.DelayedAttackData
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

class DelayedSummonData(
    private val summonData: SummonData,
    delay: Int = 0,
) : DelayedAttackData(delay) {
    override fun apply(
        level: ServerLevel,
        entity: Mob,
        target: LivingEntity?,
    ) {
        summonData.apply(level, entity, target)
    }
}