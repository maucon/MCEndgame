package de.fuballer.mcendgame.main.component.entity.custom.attack.data

import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity

data class SoundData(
    private val sound: SoundEvent,
    private val volume: () -> Float,
    private val pitch: () -> Float,
    private val category: SoundSource,
) {
    constructor(
        sound: SoundEvent,
        volume: Float,
        pitch: Float,
        category: SoundSource,
        delay: Int = 0,
    ) : this(sound, { volume }, { pitch }, category)

    fun apply(
        level: ServerLevel,
        entity: Entity,
    ) {
        level.playSound(entity, entity.blockPosition(), sound, category, volume(), pitch())
    }
}