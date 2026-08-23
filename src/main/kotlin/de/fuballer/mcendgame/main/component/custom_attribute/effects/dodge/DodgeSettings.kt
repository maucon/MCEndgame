package de.fuballer.mcendgame.main.component.custom_attribute.effects.dodge

import de.fuballer.mcendgame.main.component.entity.custom.attack.data.particle.ParticleData
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.phys.Vec3

object DodgeSettings {
    val BYPASS_DODGE = listOf(
        DamageTypes.IN_FIRE,
        DamageTypes.CAMPFIRE,
        DamageTypes.ON_FIRE,
        DamageTypes.LAVA,
        DamageTypes.HOT_FLOOR,
        DamageTypes.IN_WALL,
        DamageTypes.CRAMMING,
        DamageTypes.DROWN,
        DamageTypes.STARVE,
        DamageTypes.CACTUS,
        DamageTypes.FALL,
        DamageTypes.ENDER_PEARL,
        DamageTypes.FLY_INTO_WALL,
        DamageTypes.FELL_OUT_OF_WORLD,
        DamageTypes.GENERIC,
        DamageTypes.MAGIC,
        DamageTypes.WITHER,
        DamageTypes.DRAGON_BREATH,
        DamageTypes.DRY_OUT,
        DamageTypes.SWEET_BERRY_BUSH,
        DamageTypes.FREEZE,
        DamageTypes.STALAGMITE,
        DamageTypes.THORNS,
        DamageTypes.EXPLOSION,
        DamageTypes.PLAYER_EXPLOSION,
        DamageTypes.BAD_RESPAWN_POINT,
        DamageTypes.OUTSIDE_BORDER,
        DamageTypes.GENERIC_KILL,
    )

    val SOUND = SoundEvents.PLAYER_ATTACK_NODAMAGE

    val PARTICLES = ParticleData(
        particle = { _, _ -> ParticleTypes.WHITE_SMOKE },
        offset = { entity -> Vec3(0.0, (entity.bbHeight / 2).toDouble(), 0.0) },
        count = 5,
        dist = { Vec3.ZERO },
        speed = 0.1,
    )
}