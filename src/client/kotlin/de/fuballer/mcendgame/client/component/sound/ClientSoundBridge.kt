package de.fuballer.mcendgame.client.component.sound

import de.fuballer.mcendgame.main.component.sound.SoundBridge
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import kotlin.random.Random

class ClientSoundBridge : SoundBridge {
    override fun playRangeDefinedSound(
        sound: SoundEvent,
        category: SoundSource,
        volume: Float,
        pitch: Float,
        pos: BlockPos,
        range: Double,
    ) {
        val instance = RangeDefinedSoundInstance(
            sound,
            category,
            volume,
            pitch,
            RandomSource.create(Random.Default.nextLong()),
            pos.x + 0.5,
            pos.y + 0.5,
            pos.z + 0.5,
            range,
        )
        Minecraft.getInstance().soundManager.play(instance)
    }
}