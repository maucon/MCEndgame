package de.fuballer.mcendgame.util

import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile
import org.bukkit.util.Vector

object EnemyUtil {
    fun shootProjectile(
        shooter: LivingEntity,
        oldProjectile: AbstractArrow,
        target: LivingEntity,
        newProjectileType: EntityType,
        sound: Sound
    ): Projectile {
        val newProjectile = shooter.world.spawnEntity(oldProjectile.location, newProjectileType, false) as Projectile
        newProjectile.shooter = shooter

        val targetPosition = target.eyeLocation.toVector().add(target.location.toVector()).divide(Vector(2, 2, 2))
        newProjectile.velocity = targetPosition.subtract(oldProjectile.location.toVector()).normalize()

        shooter.world.playSound(shooter.location, sound, SoundCategory.HOSTILE, 1f, 1f)

        return newProjectile
    }
}