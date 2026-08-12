package de.fuballer.mcendgame.main.component.entity.custom.attack.data.status_effect

import de.fuballer.mcendgame.main.component.entity.custom.attack.data.DelayedAttackData
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

class DelayedStatusEffectData(
    private val effectData: StatusEffectData,
    delay: Int = 0,
) : DelayedAttackData(delay) {
    override fun apply(
        level: ServerLevel,
        entity: Mob,
        target: LivingEntity?,
    ) {
        effectData.apply(entity)
    }
}