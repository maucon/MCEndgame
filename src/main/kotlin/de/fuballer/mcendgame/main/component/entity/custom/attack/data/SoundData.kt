package de.fuballer.mcendgame.main.component.entity.custom.attack.data

import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level

open class SoundData(
    protected val sound: SoundEvent,
    protected val volume: () -> Float,
    protected val pitch: () -> Float,
    protected val category: SoundSource,
) {
    constructor(
        sound: SoundEvent,
        volume: Float,
        pitch: Float,
        category: SoundSource,
    ) : this(sound, { volume }, { pitch }, category)

    open fun apply(
        level: ServerLevel,
        entity: Entity,
    ) {
        level.playSound(entity, entity.blockPosition(), sound, category, volume(), pitch())
    }

    open fun applyClient(
        level: Level,
        entity: Entity,
        distanceDelay: Boolean,
    ) {
        level.playLocalSound(entity.x, entity.y, entity.z, sound, category, volume(), pitch(), distanceDelay)
    }
}