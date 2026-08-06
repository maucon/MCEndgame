package de.fuballer.mcendgame.main.component.entity.custom.attack.data

import de.fuballer.mcendgame.main.component.sound.RangeDefinedSoundPayload
import de.fuballer.mcendgame.main.component.sound.Sounds
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.Level

class RangeDefinedSoundData(
    sound: SoundEvent,
    volume: () -> Float,
    pitch: () -> Float,
    category: SoundSource,
    private val range: Double,
) : SoundData(sound, volume, pitch, category) {
    constructor(
        sound: SoundEvent,
        volume: Float,
        pitch: Float,
        category: SoundSource,
        range: Double,
    ) : this(sound, { volume }, { pitch }, category, range)

    override fun apply(
        level: ServerLevel,
        entity: Entity,
    ) {
        val x = entity.x
        val y = entity.y
        val z = entity.z
        val rangeSq = range * range

        val payload = RangeDefinedSoundPayload(
            entity.blockPosition(),
            BuiltInRegistries.SOUND_EVENT.wrapAsHolder(sound),
            volume(),
            pitch(),
            category,
            range,
        )

        for (player in level.players()) {
            val dx = player.x - x
            val dy = player.y - y
            val dz = player.z - z

            if (dx * dx + dy * dy + dz * dz > rangeSq) continue
            ServerPlayNetworking.send(player, payload)
        }
    }

    override fun applyClient(
        level: Level,
        entity: Entity,
        distanceDelay: Boolean,
    ) {
        Sounds.playRangeDefinedSound(
            sound,
            category,
            volume(),
            pitch(),
            entity.blockPosition(),
            range,
        )
    }
}