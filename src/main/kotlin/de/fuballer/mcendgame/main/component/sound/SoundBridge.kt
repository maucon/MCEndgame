package de.fuballer.mcendgame.main.component.sound

import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource

interface SoundBridge {
    fun playRangeDefinedSound(
        sound: SoundEvent,
        category: SoundSource,
        volume: Float,
        pitch: Float,
        pos: BlockPos,
        range: Double,
    )
}