package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.util.extension.ProjectileExtension.getAddedBaseDamage
import de.fuballer.mcendgame.util.extension.ProjectileExtension.setAddedBaseDamage
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

        val addedDamage = oldProjectile.getAddedBaseDamage() ?: 0.0
        val damage = oldProjectile.damage + addedDamage
        newProjectile.setAddedBaseDamage(damage)

        return newProjectile
    }
}