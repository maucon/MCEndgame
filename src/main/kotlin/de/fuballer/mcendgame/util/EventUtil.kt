package de.fuballer.mcendgame.util

import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Projectile
import org.bukkit.event.entity.EntityDamageByEntityEvent

object EventUtil {
    fun getPlayerDamager(event: EntityDamageByEntityEvent): Player? {
        if (event.damager is Player) {
            return event.damager as Player
        }
        if (event.damager is Projectile && (event.damager as Projectile).shooter is Player) {
            return (event.damager as Projectile).shooter as Player
        }
        return null
    }

    fun getLivingEntityDamager(event: EntityDamageByEntityEvent): LivingEntity? {
        if (event.damager is LivingEntity) {
            return event.damager as LivingEntity
        }
        if (event.damager is Projectile && (event.damager as Projectile).shooter is LivingEntity) {
            return (event.damager as Projectile).shooter as LivingEntity
        }
        return null
    }
}