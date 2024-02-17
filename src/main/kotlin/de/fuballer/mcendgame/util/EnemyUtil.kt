package de.fuballer.mcendgame.util

import de.fuballer.mcendgame.util.extension.ProjectileExtension.getAddedBaseDamage
import de.fuballer.mcendgame.util.extension.ProjectileExtension.setAddedBaseDamage
import org.bukkit.Sound
import org.bukkit.SoundCategory
import org.bukkit.entity.AbstractArrow
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Projectile

object EnemyUtil {
    fun shootCustomProjectile(
        entity: LivingEntity,
        projectile: AbstractArrow,
        newProjectileType: EntityType,
        sound: Sound
    ): Projectile {
        val newProjectile = entity.world.spawnEntity(projectile.location, newProjectileType, false) as Projectile
        newProjectile.shooter = entity
        newProjectile.velocity = projectile.velocity

        entity.world.playSound(entity.location, sound, SoundCategory.HOSTILE, 1f, 1f)

        val addedDamage = projectile.getAddedBaseDamage() ?: 0.0
        val damage = projectile.damage + addedDamage
        newProjectile.setAddedBaseDamage(damage)

        return newProjectile
    }
}