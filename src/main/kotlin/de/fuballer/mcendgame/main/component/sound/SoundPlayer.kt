package de.fuballer.mcendgame.main.component.sound

import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource

open class SoundPlayer {
    open fun playRangeDefinedSound(
        sound: SoundEvent,
        category: SoundSource,
        volume: Float,
        pitch: Float,
        pos: BlockPos,
        range: Double,
    ) {
        throw NotImplementedError("Tried calling client sound from server")
    }
}