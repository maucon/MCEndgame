package de.fuballer.mcendgame.client.component.sound

import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource

class RangeDefinedSoundInstance(
    sound: SoundEvent,
    source: SoundSource,
    volume: Float,
    pitch: Float,
    random: RandomSource,
    x: Double,
    y: Double,
    z: Double,
    val range: Double,
) : SimpleSoundInstance(sound, source, volume, pitch, random, x, y, z)