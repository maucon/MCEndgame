package de.fuballer.mcendgame.main.component.custom_attribute.effects.dodge

import de.fuballer.mcendgame.main.component.entity.custom.attack.data.particle.ParticleData
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.phys.Vec3

object DodgeSettings {
    val SOUND = SoundEvents.PLAYER_ATTACK_NODAMAGE

    val PARTICLES = ParticleData(
        particle = { _, _ -> ParticleTypes.WHITE_SMOKE },
        offset = { entity -> Vec3(0.0, (entity.bbHeight / 2).toDouble(), 0.0) },
        count = 5,
        dist = { Vec3.ZERO },
        speed = 0.1,
    )
}