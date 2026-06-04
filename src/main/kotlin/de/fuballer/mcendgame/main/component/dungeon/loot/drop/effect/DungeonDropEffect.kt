package de.fuballer.mcendgame.main.component.dungeon.loot.drop.effect

import de.fuballer.mcendgame.main.component.dungeon.loot.drop.ItemColor
import de.fuballer.mcendgame.main.util.extension.mixin.EntityMixinExtension.setForcedGlowColor
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
import org.joml.Vector3d
import kotlin.random.Random

class DungeonDropEffect(
    val sound: SoundEvent? = null,
    val particleType: ParticleOptions? = null,
    val glowColor: ItemColor? = null,
) {
    private var soundCategory = SoundSource.MASTER
    private var volume = 1F
    private var pitch = 1F
    private var pitchVariance = 0.05F

    private var particleCount = 5
    private var particleSpeed = 1.0
    private var particleOffset = Vector3d(0.0, 0.0, 0.0)

    private var additionalEffects: ((ItemEntity, ServerLevel) -> Unit)? = null

    fun apply(
        itemEntity: ItemEntity,
        world: ServerLevel,
    ) {
        playSound(itemEntity, world)
        spawnParticles(itemEntity, world)
        if (glowColor != null) {
            itemEntity.setGlowingTag(true)
            itemEntity.setForcedGlowColor(glowColor.intColor)
        }
        additionalEffects?.invoke(itemEntity, world)
    }

    private fun spawnParticles(
        itemEntity: Entity,
        world: ServerLevel,
    ) {
        if (particleType == null) return
        world.sendParticles(
            particleType,
            itemEntity.x,
            itemEntity.y + itemEntity.bbHeight / 2,
            itemEntity.z,
            particleCount,
            particleOffset.x,
            particleOffset.y,
            particleOffset.z,
            particleSpeed
        )
    }

    private fun playSound(
        itemEntity: ItemEntity,
        world: ServerLevel,
    ) {
        if (sound == null) return
        world.playSound(
            itemEntity,
            itemEntity.blockPosition(),
            sound,
            soundCategory,
            volume,
            pitch + pitchVariance * Random.nextDouble(-1.0, 1.0).toFloat()
        )
    }

    fun withSoundCategory(category: SoundSource): DungeonDropEffect {
        soundCategory = category
        return this
    }

    fun withVolume(volume: Float): DungeonDropEffect {
        this.volume = volume
        return this
    }

    fun withPitch(pitch: Float): DungeonDropEffect {
        this.pitch = pitch
        return this
    }

    fun withPitchVariance(variance: Float): DungeonDropEffect {
        pitchVariance = variance
        return this
    }

    fun withParticleCount(count: Int): DungeonDropEffect {
        particleCount = count
        return this
    }

    fun withParticleSpeed(speed: Double): DungeonDropEffect {
        particleSpeed = speed
        return this
    }

    fun withParticleOffset(offset: Vector3d): DungeonDropEffect {
        particleOffset = offset
        return this
    }

    fun withAdditionalEffects(effects: (ItemEntity, ServerLevel) -> Unit): DungeonDropEffect {
        additionalEffects = effects
        return this
    }
}