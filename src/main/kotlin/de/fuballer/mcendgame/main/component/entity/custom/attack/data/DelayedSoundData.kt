package de.fuballer.mcendgame.main.component.entity.custom.attack.data

import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob

class DelayedSoundData(
    private val sound: SoundEvent,
    private val volume: () -> Float,
    private val pitch: () -> Float,
    private val category: SoundSource,
    delay: Int = 0,
) : DelayedAttackData(delay) {
    constructor(
        sound: SoundEvent,
        volume: Float,
        pitch: Float,
        category: SoundSource,
        delay: Int = 0,
    ) : this(sound, { volume }, { pitch }, category, delay)

    override fun getInstance(target: LivingEntity?) = DelayedAttackDataInstance(this)

    override fun apply(
        world: ServerLevel,
        entity: Mob,
        target: LivingEntity?,
    ) {
        world.playSound(entity, entity.blockPosition(), sound, category, volume(), pitch())
    }
}