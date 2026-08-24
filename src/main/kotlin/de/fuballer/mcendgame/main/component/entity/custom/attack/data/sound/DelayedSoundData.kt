package de.fuballer.mcendgame.main.component.entity.custom.attack.data.sound

import de.fuballer.mcendgame.main.component.entity.custom.attack.data.DelayedAttackData
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

class DelayedSoundData(
    private val soundData: SoundData,
    delay: Int = 0,
) : DelayedAttackData(delay) {
    override fun apply(
        level: ServerLevel,
        entity: Mob,
        target: LivingEntity?,
    ) {
        soundData.apply(level, entity)
    }
}