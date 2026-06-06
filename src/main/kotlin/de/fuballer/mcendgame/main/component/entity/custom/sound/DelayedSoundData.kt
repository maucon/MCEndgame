package de.fuballer.mcendgame.main.component.entity.custom.sound

import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity

data class DelayedSoundData(
    private val sound: SoundEvent,
    private val volume: () -> Float,
    private val pitch: () -> Float,
    private val category: SoundSource,
    val delay: Int = 0,
) {
    constructor(
        sound: SoundEvent,
        volume: Float,
        pitch: Float,
        category: SoundSource,
        delay: Int = 0,
    ) : this(sound, { volume }, { pitch }, category, delay)

    fun getInstance() = DelayedSoundInstance(this)

    fun play(world: ServerLevel, entity: Entity) {
        world.playSound(entity, entity.blockPosition(), sound, category, volume(), pitch())
    }
}