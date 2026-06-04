package de.fuballer.mcendgame.main.component.entity.custom.sound

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity

data class DelayedSoundInstance(
    val soundData: DelayedSoundData,
) {
    var age = 0

    fun tick(
        world: ServerLevel,
        entity: Entity,
    ): Boolean {
        if (shouldCancel(entity)) return true

        age++
        if (age < soundData.delay) return false

        soundData.play(world, entity)
        return true
    }

    fun shouldCancel(entity: Entity) = !entity.isAlive
}