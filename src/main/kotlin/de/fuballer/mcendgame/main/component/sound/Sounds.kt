package de.fuballer.mcendgame.main.component.sound

import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource

object Sounds {
    private var player = SoundPlayer()

    fun setPlayer(player: SoundPlayer) {
        this.player = player
    }

    fun playRangeDefinedSound(
        sound: SoundEvent,
        category: SoundSource,
        volume: Float = 1.0f,
        pitch: Float = 1.0f,
        pos: BlockPos,
        range: Double,
    ) {
        player.playRangeDefinedSound(
            sound = sound,
            category = category,
            volume = volume,
            pitch = pitch,
            pos = pos,
            range = range,
        )
    }
}
