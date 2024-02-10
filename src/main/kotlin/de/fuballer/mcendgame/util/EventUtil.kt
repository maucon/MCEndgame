package de.fuballer.mcendgame.util

import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile

object EventUtil {
    fun getPlayerDamager(entity: LivingEntity): Player? {
        if (entity is Player) {
            return entity
        }
        if (entity is Projectile && (entity as Projectile).shooter is Player) {
            return (entity as Projectile).shooter as Player
        }
        return null
    }

    fun getLivingEntityDamager(entity: Entity): LivingEntity? {
        if (entity is LivingEntity) {
            return entity
        }
        if (entity is Projectile && entity.shooter is LivingEntity) {
            return entity.shooter as LivingEntity
        }
        return null
    }
}